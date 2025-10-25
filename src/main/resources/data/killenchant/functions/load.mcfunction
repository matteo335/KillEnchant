# Initialize objectives
scoreboard objectives add KillEnchant dummy "Kill Counter"
scoreboard objectives add Deaths dummy "Death Counter"

# Debug message (optional)
tellraw @a {"text":"[KillEnchant] Datapack loaded and objectives created!","color":"gold"}