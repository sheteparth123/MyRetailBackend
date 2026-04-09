package in.bushansirgur.billingsoftware.controller;

import com.razorpay.RazorpayException;
import in.bushansirgur.billingsoftware.io.OrderResponse;
import in.bushansirgur.billingsoftware.io.PaymentRequest;
import in.bushansirgur.billingsoftware.io.PaymentVerificationRequest;
import in.bushansirgur.billingsoftware.io.RazorpayOrderResponse;
import in.bushansirgur.billingsoftware.service.OrderService;
import in.bushansirgur.billingsoftware.service.RazorpayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final RazorpayService razorpayService;
    private final OrderService orderService;

    @PostMapping("/create-order")
    @ResponseStatus(HttpStatus.CREATED)
    public RazorpayOrderResponse createRazorpayOrder(@RequestBody PaymentRequest request) {
        try {
            return razorpayService.createOrder(request.getAmount(), request.getCurrency());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (RazorpayException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Unable to create Razorpay order. Check Razorpay keys and account status.");
        }
    }

    @PostMapping("/verify")
    public OrderResponse verifyPayment(@RequestBody PaymentVerificationRequest request) {
        return orderService.verifyPayment(request);
    }
}
