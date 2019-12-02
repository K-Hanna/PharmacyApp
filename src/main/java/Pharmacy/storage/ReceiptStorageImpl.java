package Pharmacy.storage;

import Pharmacy.type.Receipt;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ReceiptStorageImpl implements ReceiptStorage {

    private List<Receipt> receiptsList = new ArrayList<Receipt>();

    public Receipt getReceipt(long id) {
        receiptsList = deserializedReceipts();
        for (Receipt receipt : receiptsList) {
            if(receipt.getId() == id)
                return receipt;
        }
        return null;
    }

    public List<Receipt> getAllReceipts() {
        receiptsList = deserializedReceipts();
        return receiptsList;
    }

    public void addReceipt(Receipt receipt) {
        receiptsList.add(receipt);
        serializedReceipts();
    }

    public void removeReceipt(long id){

        Receipt r = getReceipt(id);
        receiptsList.remove(r);
        serializedReceipts();
    }

    private void serializedReceipts(){

        try {
            FileOutputStream fos = new FileOutputStream("receipt.txt");

            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(receiptsList);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Receipt> deserializedReceipts(){

        try {
            FileInputStream fis = new FileInputStream("receipt.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            receiptsList = (List<Receipt>) ois.readObject();

            ois.close();
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return receiptsList;
    }
}
