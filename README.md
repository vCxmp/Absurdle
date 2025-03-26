# Absurdle
This is a game that is similar to Wordle but has a few of its own twists.


Absurdle gives the impression of picking a single secret word, but instead what it actually does is consider the entire list of all possible secret words which conform to your guesses so far. Each time you guess, Absurdle prunes its internal list as little as possible, attempting to intentionally prolong the game as much as possible.

Suppose the Absurdle manager only knows the following 4-letter words.

ally, beta, cool, deal, else, flew, good, hope, ibex

In Absurdle, instead of beginning by choosing a word, the manager narrows down its set of possible answers as the player makes guesses. 

Turn 1

Step A: If the player guesses "argh" as the first word, the Absurdle manager considers all the possible patterns corresponding to the guess.

⬜⬜⬜⬜ — cool, else, flew, ibex

⬜⬜⬜🟨 — hope

⬜⬜🟨⬜ — good

🟨⬜⬜⬜ — beta, deal

🟩⬜⬜⬜ — ally

Step B: The manager picks the pattern that contains the largest number of target words (so that it can do as little pruning of the dictionary as possible). In this case, it would pick the pattern ⬜⬜⬜⬜ corresponding to the target words cool, else, flew, ibex. 

Turn 2

Step A: If the player then guesses "beta", the manager chooses between the following possible patterns.

⬜⬜⬜⬜ — cool

⬜🟨⬜⬜ — else, flew

🟨🟨⬜⬜ — ibex

Step B: The manager would pick ⬜🟨⬜⬜ corresponding to the target words else, flew. 

Turn 3

Step A: If the player then guesses "flew", the manager chooses between the following possible patterns.

⬜🟩🟨⬜ — else

🟩🟩🟩🟩 — flew

Step B: In this case, there's a tie between the possible patterns because both patterns include only 1 target word. The manager chooses the pattern ⬜🟩🟨⬜ not because it would prolong the game, but because ⬜🟩🟨⬜ appears before 🟩🟩🟩🟩 when considering the patterns in sorted order.


Turn 4

After this, there's only a single target word, else. The game ends when the player guesses the target word and the manager is left with no other option but to return the pattern 🟩🟩🟩🟩.
