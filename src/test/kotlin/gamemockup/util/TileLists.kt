package gamemockup.util

import entity.*

class TileLists {
    companion object{
        fun flamingos(): List<Animal> {
            return listOf(
                Animal(Type.NONE, Species.F),
                Animal(Type.NONE, Species.F),
                Animal(Type.NONE, Species.F),
                Animal(Type.NONE, Species.F),
                Animal(Type.NONE, Species.F),
                Animal(Type.NONE, Species.F),
                Animal(Type.NONE, Species.F),
                Animal(Type.FEMALE, Species.F),
                Animal(Type.FEMALE, Species.F),
                Animal(Type.MALE, Species.F),
                Animal(Type.MALE, Species.F),
            )
        }
        fun kangaroos(): List<Animal> {
            return listOf(
                Animal(Type.NONE, Species.U),
                Animal(Type.NONE, Species.U),
                Animal(Type.NONE, Species.U),
                Animal(Type.NONE, Species.U),
                Animal(Type.NONE, Species.U),
                Animal(Type.NONE, Species.U),
                Animal(Type.NONE, Species.U),
                Animal(Type.FEMALE, Species.U),
                Animal(Type.FEMALE, Species.U),
                Animal(Type.MALE, Species.U),
                Animal(Type.MALE, Species.U),
            )
        }
        fun chimpanzees(): List<Animal> {
            return listOf(
                Animal(Type.NONE, Species.S),
                Animal(Type.NONE, Species.S),
                Animal(Type.NONE, Species.S),
                Animal(Type.NONE, Species.S),
                Animal(Type.NONE, Species.S),
                Animal(Type.NONE, Species.S),
                Animal(Type.NONE, Species.S),
                Animal(Type.FEMALE, Species.S),
                Animal(Type.FEMALE, Species.S),
                Animal(Type.MALE, Species.S),
                Animal(Type.MALE, Species.S),
            )
        }
        fun pandas(): List<Animal> {
            return listOf(
                Animal(Type.NONE, Species.P),
                Animal(Type.NONE, Species.P),
                Animal(Type.NONE, Species.P),
                Animal(Type.NONE, Species.P),
                Animal(Type.NONE, Species.P),
                Animal(Type.NONE, Species.P),
                Animal(Type.NONE, Species.P),
                Animal(Type.FEMALE, Species.P),
                Animal(Type.FEMALE, Species.P),
                Animal(Type.MALE, Species.P),
                Animal(Type.MALE, Species.P),
            )
        }
        fun camels(): List<Animal> {
            return listOf(
                Animal(Type.NONE, Species.K),
                Animal(Type.NONE, Species.K),
                Animal(Type.NONE, Species.K),
                Animal(Type.NONE, Species.K),
                Animal(Type.NONE, Species.K),
                Animal(Type.NONE, Species.K),
                Animal(Type.NONE, Species.K),
                Animal(Type.FEMALE, Species.K),
                Animal(Type.FEMALE, Species.K),
                Animal(Type.MALE, Species.K),
                Animal(Type.MALE, Species.K),
            )
        }
        fun leopards(): List<Animal> {
            return listOf(
                Animal(Type.NONE, Species.L),
                Animal(Type.NONE, Species.L),
                Animal(Type.NONE, Species.L),
                Animal(Type.NONE, Species.L),
                Animal(Type.NONE, Species.L),
                Animal(Type.NONE, Species.L),
                Animal(Type.NONE, Species.L),
                Animal(Type.FEMALE, Species.L),
                Animal(Type.FEMALE, Species.L),
                Animal(Type.MALE, Species.L),
                Animal(Type.MALE, Species.L),
            )
        }
        fun zebras(): List<Animal> {
            return listOf(
                Animal(Type.NONE, Species.Z),
                Animal(Type.NONE, Species.Z),
                Animal(Type.NONE, Species.Z),
                Animal(Type.NONE, Species.Z),
                Animal(Type.NONE, Species.Z),
                Animal(Type.NONE, Species.Z),
                Animal(Type.NONE, Species.Z),
                Animal(Type.FEMALE, Species.Z),
                Animal(Type.FEMALE, Species.Z),
                Animal(Type.MALE, Species.Z),
                Animal(Type.MALE, Species.Z),
            )
        }
        fun elephants(): List<Animal> {
            return listOf(
                Animal(Type.NONE, Species.E),
                Animal(Type.NONE, Species.E),
                Animal(Type.NONE, Species.E),
                Animal(Type.NONE, Species.E),
                Animal(Type.NONE, Species.E),
                Animal(Type.NONE, Species.E),
                Animal(Type.NONE, Species.E),
                Animal(Type.FEMALE, Species.E),
                Animal(Type.FEMALE, Species.E),
                Animal(Type.MALE, Species.E),
                Animal(Type.MALE, Species.E),
            )

        }

        fun vendingStalls(): List<VendingStall> {
            return listOf(
                VendingStall(StallType.VENDING1),
                VendingStall(StallType.VENDING1),
                VendingStall(StallType.VENDING1),
                VendingStall(StallType.VENDING2),
                VendingStall(StallType.VENDING2),
                VendingStall(StallType.VENDING2),
                VendingStall(StallType.VENDING3),
                VendingStall(StallType.VENDING3),
                VendingStall(StallType.VENDING3),
                VendingStall(StallType.VENDING4),
                VendingStall(StallType.VENDING4),
                VendingStall(StallType.VENDING4),
            )
        }
        fun coins(): List<Coin> {
            return listOf(
                Coin(),
                Coin(),
                Coin(),
                Coin(),
                Coin(),
                Coin(),
                Coin(),
                Coin(),
                Coin(),
                Coin(),
                Coin(),
                Coin(),
            )
        }
    }
}