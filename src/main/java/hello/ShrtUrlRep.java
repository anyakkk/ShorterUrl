package hello;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ShrtUrlRep extends CrudRepository<ShrtUrl, String> {
    ShrtUrl getByFullUrl(String fullUrl);
}