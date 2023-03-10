package com.example.binarybandits.controllers;

import android.util.Pair;

import com.example.binarybandits.models.Player;
import com.google.zxing.Result;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 */
public class ScannerController {
    String[] animalNames = new String[] {
            "Lion", "Tiger", "Leopard", "Cheetah", "Jaguar",
            "Elephant", "Rhino", "Hippopotamus", "Giraffe", "Zebra",
            "Kangaroo", "Koala", "Wallaby", "Wombat", "TasmanianDevil",
            "Gorilla", "Chimpanzee", "Orangutan", "Bonobo", "Gibbon",
            "Panda", "Polar Bear", "Grizzly Bear", "Kodiak Bear", "BlackBear",
            "Wolf", "Coyote", "Fox", "Jackal", "Hyena",
            "Crocodile", "Alligator", "Turtle", "Tortoise", "Lizard",
            "Snake", "Python", "Boa", "Cobra", "Viper",
            "Shark", "Whale", "Dolphin", "Porpoise", "Seal",
            "Walrus", "Penguin", "Seagull", "Pelican", "Albatross",
            "Eagle", "Hawk", "Falcon", "Owl", "Sparrow",
            "Stork", "Swan", "Pelican", "Heron", "Flamingo",
            "Bee", "Ant", "Butterfly", "Moth", "Ladybug",
            "Spider", "Scorpion", "Crab", "Lobster", "Octopus",
            "Jellyfish", "Starfish", "Clam", "Oyster", "Snail",
            "Goat", "Sheep", "Cow", "Horse", "Pig",
            "Deer", "Elk", "Moose", "Caribou", "Bison",
            "Rabbit", "Hare", "Squirrel", "Chipmunk", "Raccoon",
            "Badger", "Beaver", "Otter", "Marten", "Ferret",
            "Bat", "Rat", "Mouse", "Hamster", "GuineaPig",
            "Chinchilla", "Hedgehog", "FennecFox", "Meerkat", "RedPanda"
    };

    String[] adjectives = new String[] {
            "Amazing", "Brilliant", "Clever", "Dazzling", "Elegant",
            "Fantastic", "Glorious", "Hilarious", "Incredible", "Jovial",
            "Kind", "Lively", "Magnificent", "Nimble", "Outstanding",
            "Passionate", "Quirky", "Radiant", "Spectacular", "Terrific",
            "Unique", "Vibrant", "Witty", "Pretty", "Youthful",
            "Zealous", "Adventurous", "Blissful", "Candid", "Dynamic",
            "Energetic", "Fearless", "Graceful", "Harmonious", "Inventive",
            "Jubilant", "Keen", "Luminous", "Majestic", "Nurturing",
            "Optimistic", "Playful", "QuickWitted", "Resourceful", "Serene",
            "Thoughtful", "Uplifting", "Versatile", "Whimsical", "Humongous",
            "Yummy", "Zesty", "Amiable", "Benevolent", "Courageous",
            "Diligent", "Ebullient", "Fearless", "Generous", "Honest",
            "Industrious", "Jolly", "Kooky", "Lovable", "Mirthful",
            "Noble", "Open-minded", "Passionate", "Quaint", "Reverent",
            "Sincere", "Trustworthy", "Understanding", "Versatile", "Wise",
            "Crazy", "Yearning", "Zany", "Ambitious", "Brave",
            "Compassionate", "Daring", "Empathetic", "Fierce", "Gentle",
            "Humble", "Inquisitive", "Joyful", "Knowledgeable", "Loyal",
            "Modest", "Noble", "Optimistic", "Patient", "Respectful",
            "Selfless", "Tireless", "Unassuming", "Vigilant", "Warmhearted",
            "Xenial", "Youthful", "Zealous"
    };

    String[] preAdjectives = {"Mega", "Ultra", "Super", "Hyper", "Giga", "Atomic",
            "Cosmic", "Colossal", "Deluxe", "Epic", "Extreme", "Mighty"
    };
}
