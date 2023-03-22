package com.example.postcardshop.data.repositories;

import com.example.postcardshop.data.enties.Postcard;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostcardRepository extends CrudRepository<Postcard, Long> {

  List<Postcard> findAll();
}
