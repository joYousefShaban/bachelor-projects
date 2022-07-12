-------------------------------------------------------------------------------
--
-- Title       : ALU
-- Design      : systemv1
-- Author      : Owner
-- Company     : UPIT
--
-------------------------------------------------------------------------------
--
-- File        : ALU.vhd
-- Generated   : Fri Feb  4 02:00:40 2022
-- From        : interface description file
-- By          : Itf2Vhdl ver. 1.20
--
-------------------------------------------------------------------------------
--
-- Description : 
--
-------------------------------------------------------------------------------

--{{ Section below this comment is automatically maintained
--   and may be overwritten
--{entity {ALU} architecture {ALU_a}}

library IEEE;
use IEEE.STD_LOGIC_1164.all;  
use IEEE.STD_LOGIC_UNSIGNED.all;

entity ALU is
	 port(
		 comOperation : in std_logic_vector(1 downto 0);	 --UPDATE HERE: must be extended to 2 bits to suport 3 inputs!(suport MAX 4 inputs)
		 operand1 : in STD_LOGIC_VECTOR(31 downto 0);
		 operand2 : in STD_LOGIC_VECTOR(31 downto 0);
		 zero : out STD_LOGIC;
		 sign : out STD_LOGIC;
		 result : out STD_LOGIC_VECTOR(31 downto 0)
	     );
end ALU;

--}} End of automatically maintained section

architecture ALU_a of ALU is
signal signedOperand1, signedOperand2 : std_logic_vector(32 downto 0);
signal resultAdd, resultSub, resultAnd, resultOr  : std_logic_vector(32 downto 0);  --UPDATE HERE: add new result for AND operation
signal finalResult : std_logic_vector(32 downto 0);
begin
	signedOperand1 <= '0'&operand1; --& is te concatenation operator we neeed to add 0 before the number for sign
	signedOperand2 <= '0'&operand2;	  
	
	
	resultAdd <= signedOperand1 + signedOperand2;
	resultSub <= signedOperand1 - signedOperand2;  
	--UPDATE HERE: new instruction AND is implemented
	resultAnd <= signedOperand1 AND signedOperand2;	
	resultOr  <= signedOperand1 OR signedOperand2;	
	  
	--results selection	   
	with comOperation select
	finalResult <= resultAdd when "00",
	resultSub when "01",	   
	--UPDATE HERE: new multiplexer must be extended to suport 3 inputs !
	resultAnd when "10", 
	resultOr when "11",
	(OTHERS=>'0') when others; 
	--for 4 instruction 00 - add 01 - sub 10 - and 11 - instr4	 
	--for 5 instruction 000 001 010 011 100
	--for 6 instruction 000 - add 001 sub 010 and 011 instr 4  100 instr 5 101 instr 6
	
	
	--zero
		
	process(finalResult)
	begin
		if finalResult = X"00000000" then
			zero <= '1';
		else
			zero <= '0';
		end if;
	end process;
		
	--sign
	sign <= finalResult(32);
	
	result <= finalResult(31 downto 0);

end ALU_a;
