package com.genesis.resources.utils;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

@Component
public class PersonIDProvider
{
    private static final Logger logger = LoggerFactory.getLogger(PersonIDProvider.class);

    private static final Set<String> validPersonIDs = new HashSet<>();

    public static boolean isValid(String personID)
    {
        return validPersonIDs.contains(personID);
    }

    @PostConstruct
    public void load()
    {
        logger.info("Loading PersonID whitelist from dataPersonID.txt");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("dataPersonID.txt"))))
        {
            reader.lines().map(String::trim).filter(s -> !s.isEmpty()).forEach(validPersonIDs::add);

            logger.info("Loaded {} valid PersonIDs", validPersonIDs.size());
        } catch (Exception e)
        {
            logger.error("Failed to load dataPersonID.txt", e);
            throw new IllegalStateException("Failed to load dataPersonID.txt", e);
        }
    }
}
