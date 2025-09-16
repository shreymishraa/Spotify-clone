import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Solution {

    // Map to store recipes: Potion -> List of [List of Ingredients]
    private static final Map<String, List<List<String>>> recipes = new HashMap<>();
    // Set to quickly identify which names are potions
    private static final Set<String> isPotion = new HashSet<>();
    // Memoization cache to store the calculated minimum orbs for any item
    private static final Map<String, Integer> memo = new HashMap<>();
    // A set to detect cycles during recursion
    private static final Set<String> visiting = new HashSet<>();

    /**
     * Recursively calculates the minimum orbs required to brew a given item.
     * Uses memoization to cache results and avoid redundant calculations.
     * @param name The name of the potion or item.
     * @return The minimum number of orbs required.
     */
    private static int getMinOrbs(String name) {
        // 1. Memoization: If we've already calculated the cost, return it.
        if (memo.containsKey(name)) {
            return memo.get(name);
        }

        // 2. Cycle Detection: If we're already trying to brew this in the current
        //    chain, we have a cycle. This path is impossible.
        if (visiting.contains(name)) {
            return Integer.MAX_VALUE;
        }

        // 3. Base Case: If the item is not a potion, it's a basic ingredient. Cost is 0.
        if (!isPotion.contains(name)) {
            memo.put(name, 0);
            return 0;
        }

        // Mark the current potion as being visited
        visiting.add(name);

        int minCost = Integer.MAX_VALUE;

        // Explore all recipes for the current potion
        for (List<String> recipe : recipes.get(name)) {
            // Cost for the current brewing step
            long currentRecipeCost = recipe.size() - 1;

            // Add the cost of brewing each ingredient
            for (String ingredient : recipe) {
                int ingredientCost = getMinOrbs(ingredient);
                if (ingredientCost == Integer.MAX_VALUE) { // Path is impossible
                    currentRecipeCost = Integer.MAX_VALUE;
                    break;
                }
                currentRecipeCost += ingredientCost;
            }
            
            // Update the overall minimum cost for this potion
            minCost = (int) Math.min(minCost, currentRecipeCost);
        }

        // Unmark the potion as we are leaving its recursive call
        visiting.remove(name);

        // Cache the final minimum cost before returning
        memo.put(name, minCost);
        return minCost;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine());

        // Parse all N recipes
        for (int i = 0; i < n; i++) {
            String line = sc.nextLine();
            String[] parts = line.split("=");
            String potionName = parts[0];
            String[] ingredientsArray = parts[1].split("\\+");
            List<String> ingredients = new ArrayList<>(Arrays.asList(ingredientsArray));

            isPotion.add(potionName);
            recipes.computeIfAbsent(potionName, k -> new ArrayList<>()).add(ingredients);
        }

        String targetPotion = sc.nextLine();
        
        // Calculate and print the result
        System.out.println(getMinOrbs(targetPotion));
        
        sc.close();
    }
}