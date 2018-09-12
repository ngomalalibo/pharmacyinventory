package mgt.inventory.pharmacy.sms;

public class SendSMS
{
    
    public static final String ACCOUNT_SID = "ACcf72e45278e8b05c8e67ff5b5a38d2e4";
    public static final String AUTH_TOKEN = "fc7257d42ebe3402da8394f54638abfe";
    
    
    
    public void sendSms(String from, String to, String msg)
    {
        com.twilio.Twilio.iinit(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(new com.twilio.type.PhoneNumber(from), new com.twilio.type.PhoneNumber(to), msg).create();
        System.out.println(message.getSid());
    }
}
