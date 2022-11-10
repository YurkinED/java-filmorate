package ru.yandex.practicum.filmorate.enums.converters;
import org.springframework.core.convert.converter.Converter;
import ru.yandex.practicum.filmorate.enums.SearchingParts;

public class SearchingStringToEnumConverter implements Converter<String, SearchingParts> {
    @Override
    public SearchingParts convert(String source) {
        return SearchingParts.valueOf(source.toUpperCase());
    }
}