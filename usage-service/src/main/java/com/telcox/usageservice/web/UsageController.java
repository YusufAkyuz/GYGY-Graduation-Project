package com.telcox.usageservice.web;

import com.telcox.usageservice.dto.CdrRequest;
import com.telcox.usageservice.dto.UsageBalanceResponse;
import com.telcox.usageservice.service.UsageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usage")
public class UsageController {

    private final UsageService usageService;

    public UsageController(UsageService usageService) {
        this.usageService = usageService;
    }

    /** CDR simülatörünün çağırdığı uç: normalde Kafka'dan tüketilir, MVP'de REST ile simüle edilir. */
    @PostMapping("/cdr")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UsageBalanceResponse recordCdr(@Valid @RequestBody CdrRequest request) {
        return UsageBalanceResponse.from(usageService.recordCdr(request));
    }

    @GetMapping("/{subscriptionId}")
    public UsageBalanceResponse getBalance(@PathVariable UUID subscriptionId) {
        return UsageBalanceResponse.from(usageService.getBalance(subscriptionId));
    }
}
