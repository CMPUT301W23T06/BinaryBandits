package com.example.binarybandits.controllers;
import com.example.binarybandits.models.QRCode;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Controller class for QR codes that generates information necessary to create a QR code.
 * Outstanding issues: N/A
 */
public class QRController {
    //All code written in this class has been moved to ScannerController
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

    /**
     * Calculate the hash of a QR code using the SHA-256 algorithm
     * @param source contents of QR code
     * @return Return the hash of a QR code
     */
    public String getHash(String source) {
        byte[] hash = null;
        String hashCode = null;

        // calculating hash
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(source.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Can't calculate SHA-256");
        }

        // converting to string
        if (hash != null) {
            StringBuilder hashBuilder = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(b);
                if (hex.length() == 1) {
                    hashBuilder.append("0");
                    hashBuilder.append(hex.charAt(0));
                } else {
                    hashBuilder.append(hex.substring(hex.length() - 2));
                }
            }
            hashCode = hashBuilder.toString();
        }

        return hashCode;
    }

    /**
     * Generate a unique name based on a unique hash
     * @param hash unique hash of QR code
     * @return Return the generated name of a QR code based on its hash
     */
    public String generateUniqueName(String hash) {

        long seed = Long.parseLong(hash.substring(0, 15), 16);
        Random rand = new Random(seed);

        int preAdjIndex = rand.nextInt(preAdjectives.length);
        int adjIndex = rand.nextInt(adjectives.length);
        int animalIndex = rand.nextInt(animalNames.length);

        return preAdjectives[preAdjIndex] + adjectives[adjIndex] + animalNames[animalIndex];

    }

    /**
     * Calculate how many points a QR code is worth based on its hash
     * @param hash unique hash of QR code
     * @return Return the number of points a QR code is worth
     */
    public int calculatePoints(String hash) {
        int score = 0;
        int counter = 1;

        int i = 0;
        for (; i < hash.length()-1; i++) {
            // counting number or repetitions of character at index i
            while(hash.charAt(i) == hash.charAt(i+1)) {
                counter += 1;
                if (i >= hash.length()-2) {
                    break;
                }
                i += 1;
            }
            // converting hex to decimal
            int decimal = Integer.parseInt(Character.toString(hash.charAt(i)),16);
            decimal = decimal == 0 ? 20 : decimal;
            // updating score
            if (counter > 1) {
                score += Math.pow(decimal, counter - 1);
            }
            counter = 1;
        }

        return score;
    }

}
