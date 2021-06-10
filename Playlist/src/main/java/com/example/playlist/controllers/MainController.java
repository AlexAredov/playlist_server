package com.example.playlist.controllers;
import com.example.playlist.Model.Person;
import com.example.playlist.repasitory.PersonRepository;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.*;



@RestController
public class MainController {


    private final PersonRepository repository;

    @Autowired
    public MainController(PersonRepository repository) {
        this.repository = repository;
    }
    @CrossOrigin
    @GetMapping("/full")
    List all() {
        return repository.findaLL();
    }

    @CrossOrigin
    @PostMapping("/add")
    Integer newEmployee(@RequestBody String sss) {
        String name = "";
        String email = "";
        String password = "";
        String[] subStr;
        String delimeter = " ";
        subStr = sss.split(delimeter);
        name = subStr[0];
        email = subStr[1];
        password = subStr[2];
        name = name.replace("\"", "");
        password = password.replace("\"", "");
        repository.add(name, email, password);
        return 1;
    }

    @CrossOrigin
    @GetMapping("/check/{name}")
    Person check(@PathVariable String name) {
        String pass = "";
        Person p = null;
        pass = repository.check(name);
        p = new Person(name, pass);
        return p;
    }

    @CrossOrigin
    @GetMapping("/songs/{name}")
    Person songs(@PathVariable String name) {
        String songs = "";
        Person p = null;
        songs = repository.songs(name);
        p = new Person(name, songs);
        return p;
    }

    @CrossOrigin
    @GetMapping("/friends/{name}")
    Person friends(@PathVariable String name) {
        String friends = "";
        Person p = null;
        friends = repository.friends(name);
        p = new Person(name, friends);
        return p;
    }

    @CrossOrigin
    @GetMapping("/songs")
    List songss() {
        return repository.songss();
    }

    @CrossOrigin
    @PostMapping("/addsongs/{name}")
    Integer addSongs(@PathVariable String name, @RequestBody String s) {
        repository.addS(name, s);
        return 1;
    }

    @CrossOrigin
    @PostMapping("/adsng")
    Integer adsng(@RequestBody String s) {
        repository.addSong(s);
        return 1;
    }

    @CrossOrigin
    @PostMapping("/fr/{name}")
    Integer addFr(@PathVariable String name, @RequestBody String s) {
        repository.addFr(name, s);
        return 1;
    }

    @CrossOrigin
    @PostMapping("/serves/{name}/{serves}")
    Integer serves(@PathVariable String name, @PathVariable String serves) {
        repository.serves(name, serves);
        return 1;
    }

    @CrossOrigin
    @GetMapping("/service/{name}")
    Person serv(@PathVariable String name) {
        String service = "";
        Person p = null;
        service =  repository.service(name);
        p = new Person(name, service);
        return p;
    }

    @CrossOrigin
    @PostMapping("/top/{name}/{number}")
    Integer top(@PathVariable String name, @PathVariable String number) {
        repository.top(name, number);
        return 1;
    }

    @CrossOrigin
    @GetMapping("/top/{name}")
    Person top1(@PathVariable String name) {
        String number = "";
        Person p = null;
        number =  repository.top1(name);
        p = new Person(name, number);
        return p;
    }

    @CrossOrigin
    @GetMapping("/apple/{name}")
    List Apple(@PathVariable String name) {
        String a = "";
        List ll = null;
        try {
            ll = new LinkedList();
            String url = "https://amp-api.music.apple.com/v1/catalog/ru/search?term={string}&types=activities%2Csongs%2Cstations%2Ctv-episodes%2Cuploaded-videos&limit=25&relate%5Balbums%5D=artists&relate%5Beditorial-items%5D=contents&include%5Beditorial-items%5D=contents&extend=artistUrl&fields%5Bartists%5D=url%2Cname%2Cartwork&fields%5Balbums%5D=artistName%2CartistUrl%2Cartwork%2CcontentRating%2CeditorialArtwork%2CeditorialNotes%2Cname%2CplayParams%2CreleaseDate%2Curl&with=serverBubbles%2Clyrics&l=en-gb&platform=web&art%5Burl%5D=f&omit%5Bresource%5D=autos";
            url = url.replace("{string}", name);
            HttpResponse<JsonNode> response = Unirest.get(url)
                    .header("authorization", "Bearer eyJhbGciOiJFUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IldlYlBsYXlLaWQifQ.eyJpc3MiOiJBTVBXZWJQbGF5IiwiaWF0IjoxNjE0ODE5OTU4LCJleHAiOjE2MzAzNzE5NTh9.a6ng_rkuWoGMdLIeJXLi_m52QWpeXAIAEi1pJrl6AwgZnAS0LBSZswvmUYSSvBFJtfCngUcUjdYsKTyVw7EPTg")
                    .header("Referer", "https://music.apple.com/")
                    .header("accept", "*/*")
                    .header("accept-encoding", "gzip, deflate, br")
                    .asJson();

            JSONObject guysJSON = response.getBody().getObject();
            Iterator<?> keys = guysJSON.keys();
            Object value = guysJSON.get((String)keys.next());
            JSONObject id = null;
            JSONArray data = null;

            if (value instanceof JSONObject) {
                JSONObject guyJSON = (JSONObject) value;
                id = guyJSON.getJSONObject("top");
                data = id.getJSONArray("data");
                id = (JSONObject)data.get(0);
                id = id.getJSONObject("attributes");
                a = id.get("url").toString();
            }
            ll.add(a);
            //System.out.println(a);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return ll;
    }

    @CrossOrigin
    @GetMapping("/spotify/{name}")
    List Spotify(@PathVariable String name) {
        String client_id = "2ba40caca9544994a58ce2612dee3682";
        String client_secret = "be601141c5424bb0a33667c7e09aab85";
        String token_url = "https://accounts.spotify.com/api/token";
        String s = "";
        List ll = null;

        String sss = client_id + ":" + client_secret;
        String encodedString = Base64.getEncoder().encodeToString(sss.getBytes());
        HttpResponse<JsonNode> response = Unirest.post(token_url)
                .header("Authorization", "Basic " + encodedString)
                .field("grant_type", "client_credentials")
                .asJson();
        JSONObject r = response.getBody().getObject();
        String a = r.get("access_token").toString();
        String url = "https://api.spotify.com/v1/search?type=artist%2Ctrack&q={string}&best_match=true";
        url = url.replace("{string}", name);

        HttpResponse<JsonNode> link = Unirest.get(url)
                .header("Authorization", "Bearer " + a)
                .asJson();

        JSONObject guysJSON = link.getBody().getObject();
        Iterator<?> keys = guysJSON.keys();
        Object value = guysJSON.get((String)keys.next());
        JSONObject id = null;
        JSONArray data = null;
        if (value instanceof JSONObject) {
            ll = new LinkedList();
            JSONObject guyJSON = (JSONObject) value;
            data = guyJSON.getJSONArray("items");
            id = (JSONObject)data.get(0);
            id = id.getJSONObject("external_urls");
            s = id.get("spotify").toString();
        }
        ll.add(s);
        //s = "spotify:" + s;
        return ll;
    }

    @CrossOrigin
    @GetMapping("/vk/{name}")
    List Vk(@PathVariable String name) {
        String s = "";
        List ll = null;
        s = get_link_VK(name, get_access_token_VK("aaredov@icloud.com", "playlist123"));
        ll = new LinkedList();
        ll.add(s);
        //s = "spotify:" + s;
        return ll;
    }

    private static String get_link_VK(String str, String access_token){
        String url_template = "https://vk.com/audio";
        String id = get_song_id_VK(str, access_token);
        String link;
        if(id.equals("0")){
            return str;
        }
        else{
            link = url_template + id;
        }
        return link;
    }

    private static String get_access_token_VK(String username, String password){
        String token_url = "https://oauth.vk.com/token?grant_type=password&client_id=2274003&client_secret=hHbZxrka2uZ6jB1inYsH&username={username}&password={password}";
        token_url = token_url.replace("{username}", username);
        token_url = token_url.replace("{password}", password);
        HttpResponse<JsonNode> response = Unirest.get(token_url).asJson();
        //System.out.println(response);
        JSONObject guysJSON = response.getBody().getObject();
        Iterator<?> keys = guysJSON.keys();
        Object value = guysJSON.get((String)keys.next());
        String access_token = value.toString();
        //System.out.println(access_token);
        return access_token;
    }

    private static String get_song_id_VK(String str, String access_token){
        String  url = "https://api.vk.com/method/catalog.getAudioSearch";
        HttpResponse<JsonNode> response = Unirest.post(url)
                .header("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                .field("query", str)
                .field("need_blocks","1")
                .field("lang", "ru")
                .field("v", "5.151")
                .field("https", "1")
                .field("access_token", access_token)
                .asJson();
        JSONObject guysJSON = response.getBody().getObject();
        Iterator<?> keys = guysJSON.keys();
        //System.out.println(value);
        JSONObject id = null;
        JSONArray data = null;
        id = guysJSON.getJSONObject("response");
        //System.out.println(id);
        data = id.getJSONArray("audios");
        id = (JSONObject)data.get(0);
        id = id.getJSONObject("ads");
        String s = id.get("content_id").toString();
        //System.out.println(s);
        return s;
    }

    @CrossOrigin
    @GetMapping("/yandex/{name}")
    List Yandex(@PathVariable String name) {
        String s = "";
        List ll = null;

        String url = "https://api.music.yandex.net/search?text={string}&type=all&from=search_history&inputType=keyboard&page=0&nocorrect=false";

        url = url.replace("{string}", name);

        String url_template = "https://music.yandex.ru/track/";

        HttpResponse<JsonNode> response = Unirest.get(url)
                .header("Host", "api.music.yandex.net")
                .header("Connection", "Keep-Alive")
                .header("Accept-Encoding", "gzip")
                .header("User-Agent", "okhttp/4.9.0")
                .header("Accept", "application/json")
                .header("X-Yandex-Music-Client", "YandexMusicAndroid/24021302")
                .header("X-Yandex-Music-Device", "os=Android; os_version=7.1.2; manufacturer=google; model=G011A; clid=google-play; uuid=fbf52c680d504d739ecdb3dcd49e8e32; display_size=5.7; dpi=266; mcc=250; mnc=02; device_id=cf371c691369a9f9b69fcee3dd7c6c49")
                .header("X-Yandex-Music-Client-Now", "2021-06-08T17:06:09+08:00")
                .header("X-Yandex-Music-Content-Type", "adult")
                .header("Accept-Language", "ru")
                .header("Authorization", "OAuth 1.684931014.113758.1654678740.1623142740138.37678.ZtRRvSBp_2PtUrwa.UfgoMgvJbD8nKyi6k4UBInDbCRwlexSkXnWOm-v3gX_UoVUcTCTLetcUmwfuU34zBUHa1tF31c0tRS9tc6-Li9FqFfP6mdqYoBZIn2wq62fr.5HJz1ACgv9j6vOoTq9XAqg")
                .asJson();

        JSONObject guysJSON = response.getBody().getObject();
        //System.out.println(value);
        JSONObject id = null;
        JSONArray data = null;
        id = guysJSON.getJSONObject("result");
        id = id.getJSONObject("tracks");
        data = id.getJSONArray("results");
        id = (JSONObject)data.get(0);
        s = id.get("id").toString();
        String link = url_template + s;

        ll = new LinkedList();
        ll.add(link);
        //s = "spotify:" + s;
        return ll;
    }

}
