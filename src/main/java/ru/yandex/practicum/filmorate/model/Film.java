package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
@Data
public class Film {
    private int id;
    @NotBlank(message = "Нужно заполнить название фильма.")
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Min(1)
    private long duration;
    @NotNull
    private BaseEntity mpa;

    private Set<BaseEntity> genres = new TreeSet<>(Comparator.comparingInt(BaseEntity::getId));

    /* private Set<Integer> likes = new TreeSet<>();*/
    private int rating;

   public Film(int id, String name, String description, LocalDate releaseDate, long duration, BaseEntity mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    /*
        public boolean addLike(int userId) {
            if (!likes.contains(userId)) {
                likes.add(userId);
                log.debug("Пользователь {} поставил лайк");
                return true;
            }
            return false;
        }

        public boolean removeLike(int userId) {
            if (likes.contains(userId)) {
                likes.remove(userId);
                log.debug("Пользователь {} удалил лайк");
                return true;
            }
            return false;
        }


        public int showQuantityOfLikes() {
            return likes.size();
        }
    */
    public void addGenresToFilm(Genre genre) {
        log.info("Метод addGenresToFilm в фильме запущен {}", genre);
        genres.add(genre);
        log.info("Жанры добавлены в фильм {}", genre);
    }
}
