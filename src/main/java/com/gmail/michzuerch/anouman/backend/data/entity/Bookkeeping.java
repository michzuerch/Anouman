
package com.gmail.michzuerch.anouman.backend.data.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Entity(name = "Bookkeeping") 
@NamedEntityGraphs({ 
    @NamedEntityGraph(
        name = "BookkeepingHasKontoklasses", 
        attributeNodes = { @NamedAttributeNode("kontoklasses") }),
    @NamedEntityGraph(
        name = "BookkeepingHasMehrwertsteuercodes", 
        attributeNodes = { @NamedAttributeNode("mehrwertsteuercodes") }),
    @NamedEntityGraph(
        name = "BookkeepingHasBookEntries", 
        attributeNodes = { @NamedAttributeNode("bookEntries") })
})

public class Bookkeeping extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String description;

    @NotNull
    @Digits(integer = 4, fraction = 0, message = "Ungültiges Zahlenformat")
    @DecimalMin(value = "1950", message = "Nicht vor 1950")
    @DecimalMax(value = "2150", message = "Nicht nach 2150")
    private int year;

    @OneToMany(mappedBy = "bookkeeping", cascade = CascadeType.ALL)
    private List<Kontoklasse> kontoklasses = new ArrayList<>();

    @OneToMany(mappedBy = "bookkeeping", cascade = CascadeType.ALL)
    private List<Mehrwertsteuercode> mehrwertsteuercodes = new ArrayList<>();

    @OneToMany(mappedBy = "bookkeeping", cascade = CascadeType.ALL)
    private List<BookEntry> bookEntries = new ArrayList<>();

}
