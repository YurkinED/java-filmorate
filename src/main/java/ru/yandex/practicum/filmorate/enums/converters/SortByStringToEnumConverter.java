package ru.yandex.practicum.filmorate.enums.converters;
import org.springframework.core.convert.converter.Converter;
import ru.yandex.practicum.filmorate.enums.SortingChoices;

public class SortByStringToEnumConverter implements Converter<String, SortingChoices> {
    @Override
    public SortingChoices convert(String source) {
        return SortingChoices.valueOf(source.toUpperCase());
    }
}
