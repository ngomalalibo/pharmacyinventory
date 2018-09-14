package mgt.inventory.pharmacy.entities;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

public class IdGenerator
{
    static String  prefix;
    public static String generateId(String entity)
    {
        Random random = new Random();
        String  uuid = String.valueOf(LocalDate.now().getMonthValue()+""+random.nextInt(10000));
        System.out.println("uuid: "+uuid);
        switch (entity)
        {
            case "product": prefix="PR"; break;
            case "employee": prefix="EMP"; break;
            case "customer": prefix="CUST"; break;
            case "order": prefix="ORD"; break;
            case "purchase": prefix="PO"; break;
            case "stock": prefix="ST"; break;
            default:prefix=""; break;
        }
        System.out.println("UUID generated: "+  prefix+uuid );
        return prefix+uuid;
    }
    
}
