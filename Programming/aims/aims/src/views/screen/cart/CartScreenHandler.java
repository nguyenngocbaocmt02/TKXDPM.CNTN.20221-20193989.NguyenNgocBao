package views.screen.cart;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import common.exception.MediaNotAvailableException;
import common.exception.PlaceOrderException;
import controller.PlaceOrderController;
import controller.ViewCartController;
import entity.cart.CartMedia;
import entity.order.Order;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.popup.PopupScreen;
import views.screen.shipping.ShippingScreenHandler;

/**
 * @author anvd
 *
 */
public class CartScreenHandler extends BaseScreenHandler {
	/**
	 * place order controller will be used
	 */
	private PlaceOrderController placeOrderController;

	private static Logger LOGGER = Utils.getLogger(CartScreenHandler.class.getName());

	@FXML
	private ImageView aimsImage;

	@FXML
	private Label pageTitle;

	@FXML
	VBox vboxCart;

	@FXML
	private Label shippingFees;

	@FXML
	private Label labelAmount;

	@FXML
	private Label labelSubtotal;

	@FXML
	private Label labelVAT;

	@FXML
	private Button btnPlaceOrder;
	
	@FXML
	private Button btnPlaceRushOrder;

	/**
	 * init a stage by cart.fxml
	 * @param stage
	 * @param screenPath
	 * @throws IOException
	 */
	public CartScreenHandler(Stage stage, String screenPath) throws IOException {
		super(stage, screenPath);

		// fix relative image path caused by fxml
		File file = new File("assets/images/Logo.png");
		Image im = new Image(file.toURI().toString());
		aimsImage.setImage(im);
		
		// create placeOrderController and process the order
		this.placeOrderController = new PlaceOrderController();

		// on mouse clicked, we back to home
		aimsImage.setOnMouseClicked(e -> {
			homeScreenHandler.show();
		});

		// on mouse clicked, we start processing place order usecase
		btnPlaceOrder.setOnMouseClicked(e -> {
			LOGGER.info("Place Order button clicked");
			try {
				this.placeOrderController.isRush = false;
				requestToPlaceOrder();
			} catch (SQLException | IOException exp) {
				LOGGER.severe("Cannot place the order, see the logs");
				exp.printStackTrace();
				throw new PlaceOrderException(Arrays.toString(exp.getStackTrace()).replaceAll(", ", "\n"));
			}
			
		});
		
		// on mouse clicked on place rush order, we start processing place rush order 
			btnPlaceRushOrder.setOnMouseClicked(e -> {
				LOGGER.info("Place Order button clicked");
				try {
					this.placeOrderController.isRush = true;
					requestToPlaceOrder();
				} catch (SQLException | IOException exp) {
					LOGGER.severe("Cannot place the order, see the logs");
					exp.printStackTrace();
					throw new PlaceOrderException(Arrays.toString(exp.getStackTrace()).replaceAll(", ", "\n"));
				}		
			});
	}

	public Label getLabelAmount() {
		return labelAmount;
	}

	public Label getLabelSubtotal() {
		return labelSubtotal;
	}

	/**
	 * get view cart controller
	 */
	public ViewCartController getBController(){
		return (ViewCartController) super.getBController();
	}

	/**
	 * back to previous scene
	 * @param prevScreen
	 * @throws SQLException
	 */
	public void requestToViewCart(BaseScreenHandler prevScreen) throws SQLException {
		setPreviousScreen(prevScreen);
		setScreenTitle("Cart Screen");
		getBController().checkAvailabilityOfProduct();
		displayCartWithMediaAvailability();
		show();
	}

	/**
	 * place order correspond to current cart
	 * @throws SQLException
	 * @throws IOException
	 */
	public void requestToPlaceOrder() throws SQLException, IOException {
		try {
			if (this.placeOrderController.getListCartMedia().size() == 0){
				PopupScreen.error("You don't have anything to place");
				return;
			}
			this.placeOrderController.placeOrder();
			// display available media
			displayCartWithMediaAvailability();

			// create order
			Order order = this.placeOrderController.createOrder();

			// display shipping form
			ShippingScreenHandler ShippingScreenHandler = new ShippingScreenHandler(this.stage, Configs.SHIPPING_SCREEN_PATH, order);
			ShippingScreenHandler.setPreviousScreen(this);
			ShippingScreenHandler.setHomeScreenHandler(homeScreenHandler);
			ShippingScreenHandler.setScreenTitle("Shipping Screen");
			ShippingScreenHandler.setBController(placeOrderController);
			ShippingScreenHandler.show();

		} catch (MediaNotAvailableException e) {
			// if some media are not available then display cart and break usecase Place Order
			displayCartWithMediaAvailability();
		}
	}

	/**
	 * update cart after adding or removing medias
	 * @throws SQLException
	 */
	public void updateCart() throws SQLException{
		getBController().checkAvailabilityOfProduct();
		displayCartWithMediaAvailability();
	}

	/**
	 * update price of cart
	 */
	void updateCartAmount(){
		// calculate subtotal and amount
		int subtotal = getBController().getCartSubtotal();
		int vat = (int)((Configs.PERCENT_VAT/100)*subtotal);
		int amount = subtotal + vat;
		LOGGER.info("amount" + amount);

		// update subtotal and amount of Cart
		labelSubtotal.setText(Utils.getCurrencyFormat(subtotal));
		labelVAT.setText(Utils.getCurrencyFormat(vat));
		labelAmount.setText(Utils.getCurrencyFormat(amount));
	}
	
	/**
	 * display availability of all medias
	 */
	private void displayCartWithMediaAvailability(){
		// clear all old cartMedia
		vboxCart.getChildren().clear();

		// get list media of cart after check availability
		List lstMedia = getBController().getListCartMedia();

		try {
			for (Object cm : lstMedia) {

				// display the attribute of vboxCart media
				CartMedia cartMedia = (CartMedia) cm;
				MediaHandler mediaCartScreen = new MediaHandler(Configs.CART_MEDIA_PATH, this);
				mediaCartScreen.setCartMedia(cartMedia);

				// add spinner
				vboxCart.getChildren().add(mediaCartScreen.getContent());
			}
			// calculate subtotal and amount
			updateCartAmount();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
