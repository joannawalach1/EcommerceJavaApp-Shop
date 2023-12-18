package pl.com.coders.shop2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.com.coders.shop2.domain.Order;
import pl.com.coders.shop2.domain.User;
import pl.com.coders.shop2.domain.dto.OrderDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    //OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "user", target = "userLastName", qualifiedByName = "mapUserToLastName")
    OrderDto orderToDto(Order order);

    @Mapping(source = "userLastName", target = "user", qualifiedByName = "mapLastNameToUser")
    Order dtoToOrder(OrderDto orderDto);

    @Named("mapUserToLastName")
    default String mapUserToLastName(User user) {
        return user != null ? user.getLastName() : null;
    }

    @Named("mapLastNameToUser")
    default User mapLastNameToUser(String userLastName) {
        User user = new User();
        user.setLastName(userLastName);
        return user;
    }

    List<OrderDto> ordersToDtos(List<Order> allOrders);
}
