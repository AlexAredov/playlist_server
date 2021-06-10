package com.example.playlist.repasitory;

import com.example.playlist.Model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface PersonRepository extends JpaRepository<Person,Long> {

    @Query(value = "SELECT name FROM person", nativeQuery = true)
    List<String> findaLL();

    @Modifying
    @Query(value = "insert into person (name, email, password) VALUES (:name,:email, :password)", nativeQuery = true)
    @Transactional
    void add(@Param("name") String name, @Param("email") String email, @Param("password") String password);

    @Query(value = "SELECT PASSWORD FROM person WHERE name=:name", nativeQuery = true)
    String check(@Param("name") String name);

    @Query(value = "SELECT SONGS FROM person WHERE name=:name", nativeQuery = true)
    String songs(@Param("name") String name);

    @Query(value = "SELECT FRIENDS FROM person WHERE name=:name", nativeQuery = true)
    String friends(@Param("name") String name);

    @Query(value = "SELECT name FROM song", nativeQuery = true)
    List<String> songss();

    @Modifying
    @Query(value = "UPDATE person SET songs = :song WHERE name = :name", nativeQuery = true)
    @Transactional
    void addS(@Param("name") String name, @Param("song") String song);

    @Modifying
    @Query(value = "INSERT INTO song (name) VALUES (:song)", nativeQuery = true)
    @Transactional
    void addSong(@Param("song") String song);

    @Modifying
    @Query(value = "UPDATE person SET friends = :fr WHERE name = :name", nativeQuery = true)
    @Transactional
    void addFr(@Param("name") String name, @Param("fr") String fr);

    @Modifying
    @Query(value = "UPDATE person SET serves = :serves WHERE name = :name", nativeQuery = true)
    @Transactional
    void serves(@Param("name") String name, @Param("serves") String serves);

    @Query(value = "SELECT serves FROM person WHERE name=:name", nativeQuery = true)
    String service(@Param("name") String name);

    @Modifying
    @Query(value = "UPDATE song SET number = :number WHERE name = :name", nativeQuery = true)
    @Transactional
    void top(@Param("name") String name, @Param("number") String number);

    @Query(value = "SELECT number FROM song WHERE name=:name", nativeQuery = true)
    String top1(@Param("name") String name);
}
