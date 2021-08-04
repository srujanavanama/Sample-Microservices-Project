package com.microservice.usersdemo.resource;

import com.microservice.usersdemo.model.User;
import com.microservice.usersdemo.repository.UserRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class UserResource {
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

//    Constructor Injection
    public UserResource(UserRepository userRepository, RestTemplate restTemplate) {

        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/hello")
    public String getHello() {
        return "Hello World";
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

//    @GetMapping("/users-orders")
//    public String getUserOrders() {
//        //RestTemplate is HTTP client for Spring
//        String result = restTemplate.getForObject("http://orderms-demo/users-orders", String.class);
//        return result;
//    }

    @GetMapping("/users/{userId}/orders")
    @HystrixCommand(fallbackMethod = "getUsersFromFallback", commandProperties =
            {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")},
            // Bulkhead
            threadPoolKey = "getAllUsersThreadPool",
            threadPoolProperties = {
                @HystrixProperty(name = "coreSize", value = "30"),
                @HystrixProperty(name = "maxQueueSize", value = "10")
            })
    public Object getUserOrders(@PathVariable Long userId) {
        //RestTemplate is HTTP client for Spring
        Object orders = restTemplate.getForObject("http://orderms/orders/search/findByUserId?" + "userId=" + userId, Object.class);
        return orders;
    }

    public Object getUsersFromFallback(Long userId) {
        return "This is coming from Fallback";
    }
}
