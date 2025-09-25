package com.booleanuk;

import com.booleanuk.model.Joke;
import com.booleanuk.repository.JokeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Main implements CommandLineRunner {
    private final JokeRepository jokeRepository;

    public Main(JokeRepository jokeRepository) {
        this.jokeRepository = jokeRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        List<Joke> jokes = new ArrayList<>();
        if(this.jokeRepository.findAll().isEmpty()) {
            jokes.add(new Joke("V2h5IHdhcyB0aGUgbXVzaWNpYW4gYXJyZXN0ZWQ/IEhlIGdvdCBpbiB0cmVibGUu"));
            jokes.add(new Joke("SGUgd2FzIGEgcmVhbCBnZW50bGVtZW4gYW5kIGFsd2F5cyBvcGVuZWQgdGhlIGZyaWRnZSBkb29yIGZvciBtZQ=="));
            jokes.add(new Joke("V2hhdCBzaG91bGQgeW91IGRvIGJlZm9yZSBjcml0aWNpemluZyBQYWMtTWFuPyBXQUtBIFdBS0EgV0FLQSBtaWxlIGluIGhpcyBzaG9lcw=="));
            jokes.add(new Joke("V2h5IGRvIHlvdSBuZXZlciBzZWUgZWxlcGhhbnRzIGhpZGluZyBpbiB0cmVlcz8gJ0NhdXNlIHRoZXkgYXJlIGZyZWFraW5nIGdvb2QgYXQgaXQ="));
            jokes.add(new Joke("V2hhdCBkbyB5b3UgY2FsbCBhIHBvdGF0byBpbiBzcGFjZT8gU3B1ZG5paw=="));
            jokes.add(new Joke("V2h5IGRpZCB0aGUgY2hpY2tlbiBob2xkIGEgc2VhbmNlPyBUbyBnZXQgdG8gdGhlIG90aGVyIHNpZGUu"));
            jokes.add(new Joke("V2hlcmUgZG8gYmFieSBjb3dzIGdvIHRvIGVhdCBsdW5jaD8gQXQgdGhlIGNhbGYtZXRlcmlhLg=="));
            jokes.add(new Joke("SSBhbSBsb29raW5nIGZvcndhcmQgdG8gNnBtIFRoYW5rc2dpdmluZyBEYXkgd2hlbiBXYWxtYXJ0IG9wZW5zIGl0cyBkb29ycyBmb3IgaXRzIGFubnVhbCBzYWxlIG9mIHRyYW1wbGVkIGh1bWFuIGNvcnBzZXMu"));
            jokes.add(new Joke("V2hhdCBkaWQgdGhlIGNhciBzYWlkIHRvIHRoZSB2YWxldD8gSSd2ZSBiZWVuIHRocm91Z2ggYSBsb3Qu"));
            jokes.add(new Joke("SSB3YXMgZHJpbmtpbmcgYXQgdGhlIGJhciwgc28gSSB0b29rIGEgYnVzIGhvbWUuIFRoYXQgbWF5IG5vdCBiZSBhIGJpZyBkZWFsIHRvIHlvdSwgYnV0IEkndmUgbmV2ZXIgZHJpdmVuIGEgYnVzIGJlZm9yZSE="));
            jokes.add(new Joke("V2hhdCBkbyB5b3UgY2FsbCBhIGJsaW5kIGRpbm9zYXVyPyBBIGRvLXRoaW5rLWhlLXNhdXJ1cw=="));
            jokes.add(new Joke("TXkgZXgtd2lmZSBzdGlsbCBtaXNzZXMgbWUuLi4gQnV0IGhlciBhaW0gaXMgZ2V0dGluIGJldHRlci4="));
            jokes.add(new Joke("VHdvIHNrZXB0aWNzIHdhbGsgaW50byBhIGJhci4uIEknZCB0ZWxsIHlvdSB3aGF0IGhhcHBlbnMgbmV4dCBidXQgbm9vbmUga25vd3M="));
            jokes.add(new Joke("Kkd1eSB0cmllcyBnaXZpbmcgbWUgaGlzIHBob25lIG51bWJlciogTWU6IE9oIG5vIHRoYW5rIHlvdS4gSSBhbHJlYWR5IGhhdmUgb25l"));
            jokes.add(new Joke("V2h5IGNhbid0IGEgYmlrZSBzdGFuZCBvbiBpdCdzIG93bj8gQmVjYXVzZSBpdCBpcyB0d28gdGlyZWQu"));
            jokes.add(new Joke("TXkgaG91c2UgaXMgcmVhbGx5IHNtYWxsIHVudGlsIEkgY2FuJ3QgZmluZCBteSBwaG9uZS4="));
            jokes.add(new Joke("SG93IGRvIHRoZXkgY2FsY3VsYXRlIGdsb2JhbCB3YXJtaW5nPyBBbC1nb3JlLXl0aG1z"));
            jokes.add(new Joke("V2h5IGRpZCB0aGUgY29tcG9zZXIgZ28gdG8gdGhlIGNoaXJvcHJhY3Rvcj8gQmVjYXVzZSBoZSBoYWQgQmFjaCBwcm9ibGVtcw=="));
            jokes.add(new Joke("QSBzaGVlcCwgYSBkcnVtIGFuZCBhIHNuYWtlIGZhbGwgZG93biBhIGNsaWZmLCBiYSBkdW0gdHNz"));
            jokes.forEach(joke -> {jokeRepository.save(joke);});}
    }
}