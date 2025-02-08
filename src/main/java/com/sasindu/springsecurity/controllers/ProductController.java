package com.sasindu.springsecurity.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.url.prefix}" + "/products")
public class ProductController {
}
