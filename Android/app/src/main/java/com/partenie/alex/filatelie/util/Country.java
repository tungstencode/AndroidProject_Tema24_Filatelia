package com.partenie.alex.filatelie.util;

import java.util.Arrays;

public final class Country {
    public final String name;
    public final String[] topLevelDomain;
    public final String alpha2Code;
    public final String alpha3Code;
    public final String[] callingCodes;
    public final String capital;
    public final String[] altSpellings;
    public final String region;
    public final String subregion;
    public final long population;
    public final int[] latlng;
    public final String demonym;
    public final long area;
    public final long gini;
    public final String[] timezones;
    public final String[] borders;
    public final String nativeName;
    public final String numericCode;
    public final Currency currencies[];
    public final Language languages[];
    public final Translations translations;
    public final String flag;
    public final RegionalBloc regionalBlocs[];
    public final String cioc;

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", topLevelDomain=" + Arrays.toString(topLevelDomain) +
                ", alpha2Code='" + alpha2Code + '\'' +
                ", alpha3Code='" + alpha3Code + '\'' +
                ", callingCodes=" + Arrays.toString(callingCodes) +
                ", capital='" + capital + '\'' +
                ", altSpellings=" + Arrays.toString(altSpellings) +
                ", region='" + region + '\'' +
                ", subregion='" + subregion + '\'' +
                ", population=" + population +
                ", latlng=" + Arrays.toString(latlng) +
                ", demonym='" + demonym + '\'' +
                ", area=" + area +
                ", gini=" + gini +
                ", timezones=" + Arrays.toString(timezones) +
                ", borders=" + Arrays.toString(borders) +
                ", nativeName='" + nativeName + '\'' +
                ", numericCode='" + numericCode + '\'' +
                ", currencies=" + Arrays.toString(currencies) +
                ", languages=" + Arrays.toString(languages) +
                ", translations=" + translations +
                ", flag='" + flag + '\'' +
                ", regionalBlocs=" + Arrays.toString(regionalBlocs) +
                ", cioc='" + cioc + '\'' +
                '}';
    }

    public Country(String name, String[] topLevelDomain, String alpha2Code, String alpha3Code, String[] callingCodes, String capital, String[] altSpellings, String region, String subregion, long population, int[] latlng, String demonym, long area, long gini, String[] timezones, String[] borders, String nativeName, String numericCode, Currency[] currencies, Language[] languages, Translations translations, String flag, RegionalBloc[] regionalBlocs, String cioc){
        this.name = name;
        this.topLevelDomain = topLevelDomain;
        this.alpha2Code = alpha2Code;
        this.alpha3Code = alpha3Code;
        this.callingCodes = callingCodes;
        this.capital = capital;
        this.altSpellings = altSpellings;
        this.region = region;
        this.subregion = subregion;
        this.population = population;
        this.latlng = latlng;
        this.demonym = demonym;
        this.area = area;
        this.gini = gini;
        this.timezones = timezones;
        this.borders = borders;
        this.nativeName = nativeName;
        this.numericCode = numericCode;
        this.currencies = currencies;
        this.languages = languages;
        this.translations = translations;
        this.flag = flag;
        this.regionalBlocs = regionalBlocs;
        this.cioc = cioc;
    }

    public static final class Currency {
        public final String code;
        public final String name;
        public final String symbol;

        public Currency(String code, String name, String symbol){
            this.code = code;
            this.name = name;
            this.symbol = symbol;
        }
    }

    public static final class Language {
        public final String iso639_1;
        public final String iso639_2;
        public final String name;
        public final String nativeName;

        public Language(String iso639_1, String iso639_2, String name, String nativeName){
            this.iso639_1 = iso639_1;
            this.iso639_2 = iso639_2;
            this.name = name;
            this.nativeName = nativeName;
        }
    }

    public static final class Translations {
        public final String de;
        public final String es;
        public final String fr;
        public final String ja;
        public final String it;
        public final String br;
        public final String pt;
        public final String nl;
        public final String hr;
        public final String fa;

        public Translations(String de, String es, String fr, String ja, String it, String br, String pt, String nl, String hr, String fa){
            this.de = de;
            this.es = es;
            this.fr = fr;
            this.ja = ja;
            this.it = it;
            this.br = br;
            this.pt = pt;
            this.nl = nl;
            this.hr = hr;
            this.fa = fa;
        }
    }

    public static final class RegionalBloc {
        public final String acronym;
        public final String name;
        public final OtherAcronym otherAcronyms[];
        public final OtherName otherNames[];

        public RegionalBloc(String acronym, String name, OtherAcronym[] otherAcronyms, OtherName[] otherNames){
            this.acronym = acronym;
            this.name = name;
            this.otherAcronyms = otherAcronyms;
            this.otherNames = otherNames;
        }

        public static final class OtherAcronym {

            public OtherAcronym(){
            }
        }

        public static final class OtherName {

            public OtherName(){
            }
        }
    }
}