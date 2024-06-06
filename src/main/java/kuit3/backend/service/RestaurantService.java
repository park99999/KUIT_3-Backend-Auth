package kuit3.backend.service;

import kuit3.backend.common.exception.DatabaseException;
import kuit3.backend.common.exception.RestaurantException;
import kuit3.backend.dao.RestaurantDao;
import kuit3.backend.dto.restaurant.GetRestaurant;
import kuit3.backend.dto.restaurant.GetcategoryResponse;
import kuit3.backend.dto.restaurant.PostRestaurantRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static kuit3.backend.common.response.status.BaseExceptionResponseStatus.DATABASE_ERROR;
import static kuit3.backend.common.response.status.BaseExceptionResponseStatus.DUPLICATE_RESTAURANT_NAME;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantDao restaurantDao;
    public List<GetRestaurant> getRestaurants(String restaurantname, String category, String status) {
        log.info("[Restaurant.getRestaurants]");
        return restaurantDao.getRestaurants(restaurantname,category,status);
    }
    public long createRestaurant(PostRestaurantRequest postRestaurantRequest){
        validateRestaurantName(postRestaurantRequest.getRestaurantname());
        return restaurantDao.createRestaurant(postRestaurantRequest);
    }

    private void validateRestaurantName(String restaurantname) {
        if (restaurantDao.hasDuplicateRestaurantName(restaurantname)) {
            throw new RestaurantException(DUPLICATE_RESTAURANT_NAME);
        }
    }

    public void modifyRestaurantName(long restaurantId, String restaurantname) {
        validateRestaurantName(restaurantname);
        int affectedRows = restaurantDao.modifyRestaurantName(restaurantId, restaurantname);
        if (affectedRows != 1) {
            throw new DatabaseException(DATABASE_ERROR);
        }
    }
}
