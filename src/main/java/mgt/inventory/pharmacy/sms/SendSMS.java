package mgt.inventory.pharmacy.sms;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SendSMS
{
    
    public static final String ACCOUNT_SID = "ACcf72e45278e8b05c8e67ff5b5a38d2e4";
    public static final String AUTH_TOKEN = "fc7257d42ebe3402da8394f54638abfe";
    
    public static void sendSms(String from, String to, String msg)
    {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(new PhoneNumber(from), new PhoneNumber(to), msg).create();
        System.out.println(message.getSid());
    }
}
