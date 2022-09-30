package io.github.ololx.examples.fruits.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * project bloom-filter-implementation-example
 * created 30.09.2022 22:11
 *
 * @author Alexander A. Kropotin
 */
@Validated
@CrossOrigin(origins = "/**")
@RequestMapping(value = "fruits")
@RestController
@RequiredArgsConstructor
@FieldDefaults(
        level = AccessLevel.PRIVATE,
        makeFinal = true
)
public class FruitsController {

}
