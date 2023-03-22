package com.example.postcardshop.data.repositories;

import com.example.postcardshop.data.enties.PostcardImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostcardImageRepository extends CrudRepository<PostcardImage, Long> {}
