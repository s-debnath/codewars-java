package io.bytewize.kata.kyu.five;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * https://www.codewars.com/kata/simple-assembler-interpreter/train/java
 */
public class SimpleAssembler {

    /**
     * Model for an instruction in a program
     */
    static class Instruction {

        enum Type {
            MOV, INC, DEC, JNZ
        }

        private final Integer step;
        private final Type type;
        private final String param1;
        private final String param2;

        Instruction(int step, String instruction) {
            String[] split = instruction.split(" ");

            this.step = step;
            this.type = Type.valueOf(split[0].toUpperCase());
            this.param1 = split[1];
            this.param2 = (split.length == 3) ? split[2]: "";
        }

        public Integer getStep() {
            return step;
        }

        public Type getType() {
            return type;
        }

        public String getParam1() {
            return param1;
        }

        public String getParam2() {
            return param2;
        }

    }

    /**
     * Interprets program and produces the registers
     * @param program
     * @return registers
     */
    public static Map<String, Integer> interpret(String[] program){
        Map<String, Integer> registers = new HashMap<>();

        // Map all the Strings in the program to their instruction
        List<Instruction> instructions = IntStream.range(0, program.length)
            .mapToObj(i -> new Instruction(i, program[i]))
            .collect(Collectors.toList());

        Integer programCounter = 0;

        while (programCounter < program.length) {
           programCounter = execute(registers, instructions.get(programCounter));
        }

        return registers;
    }

    /**
     * Executes an instruction
     * @param registers
     * @param instruction
     * @return step
     */
    public static Integer execute(Map<String, Integer> registers, Instruction instruction) {

        switch (instruction.getType()) {
            case MOV: return mov(registers, instruction);
            case INC: return inc(registers, instruction);
            case DEC: return dec(registers, instruction);
            case JNZ: return jnz(registers, instruction);
            default: return 0;
        }

    }

    /**
     * Executes the mov instruction
     * @param registers
     * @param instruction
     * @return step
     */
    public static Integer mov(Map<String, Integer> registers, Instruction instruction) {
        Integer actualValue;

        // Checks for integer otherwise gets digit from other param1
        if (instruction.getParam2().matches("[-+]?\\d*\\.?\\d+")) {
            actualValue = Integer.parseInt(instruction.getParam2());
        } else {
            actualValue = registers.get(instruction.getParam2());
        }

        registers.put(instruction.getParam1(), actualValue);

        return instruction.getStep() + 1;
    }

    /**
     * Executes the inc instruction
     * @param registers
     * @param instruction
     * @return step
     */
    public static Integer inc(Map<String, Integer> registers, Instruction instruction) {
        Integer currentValue = registers.get(instruction.getParam1());

        registers.put(instruction.getParam1(), currentValue + 1);

        return instruction.getStep() + 1;
    }

    /**
     * Executes the dec instruction
     * @param registers
     * @param instruction
     * @return step
     */
    public static Integer dec(Map<String, Integer> registers, Instruction instruction) {
        Integer currentValue = registers.get(instruction.getParam1());

        registers.put(instruction.getParam1(), currentValue - 1);

        return instruction.getStep() + 1;
    }

    /**
     * Executes the jnz instruction
     * @param registers
     * @param instruction
     * @return step
     */
    public static Integer jnz(Map<String, Integer> registers, Instruction instruction) {
        Integer currentValue = registers.get(instruction.getParam1());
        Integer jumpValue = Integer.parseInt(instruction.getParam2());

        // Null means that param1 was actually a value
        if (currentValue == null) {
            currentValue = Integer.parseInt(instruction.getParam1());
        }

        if (currentValue != 0) {
            return instruction.getStep() + jumpValue;
        } else {
            return  instruction.getStep() + 1;
        }
    }

}
