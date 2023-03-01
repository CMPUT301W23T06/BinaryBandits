package com.example.binarybandits;
import android.util.Pair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

public class QRController {

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

    public String generateUniqueName(String hash) {

        long seed = Long.parseLong(hash.substring(0, 15), 16);
        Random rand = new Random(seed);

        int preAdjIndex = rand.nextInt(preAdjectives.length);
        int adjIndex = rand.nextInt(adjectives.length);
        int animalIndex = rand.nextInt(animalNames.length);

        return preAdjectives[preAdjIndex] + adjectives[adjIndex] + animalNames[animalIndex];

    }

    public String generateUniqueVisualRep(String hash) {
        long seed = Long.parseLong(hash.substring(0, 15), 16);
        Random rand = new Random(seed);
        StringBuilder visualRep = new StringBuilder();

        ArrayList<Pair<Integer, Integer>> fill = new ArrayList<>();
        int numFills = 8;
        int gridWidth = 5;
        for (int i = 0; i < numFills; i++) {
            int row = rand.nextInt(gridWidth);
            int col = rand.nextInt(gridWidth);

            fill.add(new Pair<>(row, col));
            fill.add(new Pair<>(row, gridWidth - 1 - col));
        }

        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridWidth; j++) {
                if (fill.contains(new Pair<>(i, j))) {
                    visualRep.append(". ");
                } else {
                    visualRep.append("0 ");
                }
            }
            visualRep.append("\n");
        }

        return visualRep.toString();
    }

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
            // updating score
            if (counter > 1) {
                score += Math.pow(decimal, counter - 1);
            }
            counter = 1;
        }

        return score;
    }


}
