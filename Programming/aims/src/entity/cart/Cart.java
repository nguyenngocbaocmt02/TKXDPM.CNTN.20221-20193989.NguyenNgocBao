package entity.cart;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.exception.MediaNotAvailableException;
import entity.media.Media;

/**
 * @author baonn
 *
 */
public class Cart {
    
    /**
     * list of current cart media
     */
    private List<CartMedia> lstCartMedia;
    
    /**
     * static instance for initializing a new cart
     */
    private static Cart cartInstance;

    /**
     * static method for get an instane
     * @return Cart
     */
    public static Cart getCart(){
        if(cartInstance == null) cartInstance = new Cart();
        return cartInstance;
    }

    private Cart(){
        lstCartMedia = new ArrayList<>();
    }

    /**
     * add a cart media to cart
     * @param cm
     */
    public void addCartMedia(CartMedia cm){
        lstCartMedia.add(cm);
    }

    /**
     * remove cart media
     * @param cm
     */
    public void removeCartMedia(CartMedia cm){
        lstCartMedia.remove(cm);
    }

    /**
     * remove list media
     * @return
     */
    public List<CartMedia> getListMedia(){
        return lstCartMedia;
    }

    public void emptyCart(){
        lstCartMedia.clear();
    }

    /**
     * get number of media in cart
     * @return
     */
    public int getTotalMedia(){
        int total = 0;
        for (Object obj : lstCartMedia) {
            CartMedia cm = (CartMedia) obj;
            total += cm.getQuantity();
        }
        return total;
    }

    /**
     * calculate price of cart
     * @return
     */
    public int calSubtotal(){
        int total = 0;
        for (Object obj : lstCartMedia) {
            CartMedia cm = (CartMedia) obj;
            total += cm.getPrice()*cm.getQuantity();
        }
        return total;
    }

    /**
     * check whether the number of left medias is enough for demand
     * @throws SQLException
     */
    public void checkAvailabilityOfProduct() throws SQLException{
        boolean allAvai = true;
        for (Object object : lstCartMedia) {
            CartMedia cartMedia = (CartMedia) object;
            int requiredQuantity = cartMedia.getQuantity();
            int availQuantity = cartMedia.getMedia().getQuantity();
            if (requiredQuantity > availQuantity) allAvai = false;
        }
        if (!allAvai) throw new MediaNotAvailableException("Some media not available");
    }

    /**
     * check if media in cart
     * @param media
     * @return
     */
    public CartMedia checkMediaInCart(Media media){
        for (CartMedia cartMedia : lstCartMedia) {
            if (cartMedia.getMedia().getId() == media.getId()) return cartMedia;
        }
        return null;
    }

}
