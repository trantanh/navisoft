package com.trantanh.navisoft.restaurant.configraution;

import com.trantanh.navisoft.restaurant.constant.RestaurantConstants;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ivan Tran, tran.tuan.anh@starkysclub.com
 * 21.09.2018
 */
@Configuration
@ComponentScan(RestaurantConstants.CONTROLLER)
public class AppConfig {
}
