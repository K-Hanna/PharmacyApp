package Pharmacy.storage;

import Pharmacy.type.Receipt;

import java.util.List;

public interface ReceiptStorage {

    Receipt getReceipt(long id);
    List<Receipt> getAllReceipts();
    void addReceipt(Receipt receipt);
    void removeReceipt(long id);
}
