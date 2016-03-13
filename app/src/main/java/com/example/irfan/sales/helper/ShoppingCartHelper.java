package com.example.irfan.sales.helper;

import android.content.res.Resources;

import com.example.irfan.sales.activity.ShoppingCartEntry;
import com.example.irfan.sales.object.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by Irfan on 1/2/2016.
 */
public class ShoppingCartHelper {
    public static final String PRODUCT_INDEX = "PRODUCT_INDEX";
    public static final String ORDER_INDEX = "ORDER_INDEX";
    private static List<Product> catalog;
    private static Map<Product,ShoppingCartEntry> cartMap=new HashMap<Product,ShoppingCartEntry>();
    //private static List<Product> cart;

    public static List<Product> getCatalog(Resources res) {
        if (catalog == null) {
            catalog = new Vector<Product>();
            catalog.add(new Product("SAMSUNG Galaxy Note 5","8999979026802", "SAMSUNG Galaxy Note 5 - Black Sapphire", 9300000,10,9));
            catalog.add(new Product("ACER Liquid E700 Triple SIM","12345", "5.0-inch IPS LCD capacitive touchscreen, 16GB Internal, 2GB RAM, GSM/HSDPA, Wi-Fi/Bluetooth"
                    , 2100000,15,10));
            catalog.add(new Product("HP Stream 8 - Black","12345", "Intel® Atom™ Z3735G 1.3Ghz, 8inch, 32GB storage, 1GB RAM"
                    , 2000000,4,20));
            catalog.add(new Product("ACER Aspire Switch 11V","12345", "Intel Dual Core M-5Y10c, 4GB DDR3, 60GB SSD + 500GB HDD, VGA Intel HD "
                    , 9700000,5,5));
            catalog.add(new Product("PHILIPS Speaker Bluetooth [BT3500B] - Black","12345", "Speaker Bluetooth, Microphone for hands-free phone calls, NFC, 3.5mm audio-in connection"
                    , 1290000,15,9));
            catalog.add(new Product("JABRA Sport Coach Wireless","12345", "Bluetooth Headset Stereo, Bluetooth v4.0, Support NFC, Sweat & Weather Resistant"
                    , 1830000,15,14));
            catalog.add(new Product("HP Spectre x360 13-4125TU","12345", "Intel Core i7-6500U, 8GB DDR3, 512GB SSD, VGA Integrated, 13.3\" FHD, Touchscreen"
                    , 20000000,15,10));
//            catalog.add(new Product("Switch", res.getDrawable(R.drawable.switchbook), "Switch by Chip Heath and Dan Heath", 24.99));
//            catalog.add(new Product("Watchmen", res.getDrawable(R.drawable.watchmen), "Watchment by Alan Moore and Dave Gibbons", 14.99));

        }
        return catalog;
    }

    public static void setCartMap(Map<Product, ShoppingCartEntry> cartMap) {
        ShoppingCartHelper.cartMap = cartMap;
    }

    public static void setQuantity(Product product, int quantity){
        ShoppingCartEntry curEntry = cartMap.get(product);

        if(quantity <= 0 ){
            if(curEntry !=null)
                removeProduct(product);
            return;
        }

        if(curEntry == null){
            curEntry = new ShoppingCartEntry(product,quantity);
            cartMap.put(product, curEntry);
            return;
        }

        curEntry.setQuantity(quantity);

    }

    private static void removeProduct(Product product) {
        cartMap.remove(product);
    }

    public static int getProductQuantity(Product product){
        ShoppingCartEntry curEntry  = cartMap.get(product);
        if(curEntry != null){
            return curEntry.getQuantity();
        }
        return  0;

    }
    public static List<Product> getCartList() {
     List<Product> cartList= new Vector<Product>(cartMap.keySet().size());
        for(Product p : cartMap.keySet()){
            cartList.add(p);
        }
        return cartList;
    }
}
