package com.buy_anytime.order_service.razorpay;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class RazorpayService {

    @Value("${razorpay_id}")
    private String apiKey;

    @Value("${razorpay_key}")
    private String apiSecret;

    public String createRazorpayOrder(BigDecimal amount) throws RazorpayException {
        RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecret);

        // Amount needs to be in the smallest currency unit (e.g., paise for INR)
        int amountInPaise = amount.multiply(new BigDecimal(100)).intValue();

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR"); // Or your desired currency
        orderRequest.put("receipt", "order_rcptid_" + System.currentTimeMillis());

        Order order = razorpayClient.orders.create(orderRequest);
        return order.get("id");
    }
}
