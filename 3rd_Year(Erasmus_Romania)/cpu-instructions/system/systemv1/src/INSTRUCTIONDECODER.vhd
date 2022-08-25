-------------------------------------------------------------------------------
--
-- Title       : instructionDecoder
-- Design      : systemv1
-- Author      : Owner
-- Company     : UPIT
--
-------------------------------------------------------------------------------
--
-- File        : INSTRUCTIONDECODER.vhd
-- Generated   : Fri Feb  4 02:31:52 2022
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
--{entity {instructionDecoder} architecture {instructionDecoder_a}}

library IEEE;
use IEEE.STD_LOGIC_1164.all;

entity instructionDecoder is
	 port(
	 	instruction : in STD_LOGIC_VECTOR(31 downto 0);
	 	--registers file com
	 	comWr : out STD_LOGIC;
	 	comWrSel : out STD_LOGIC_VECTOR(1 downto 0);  
	 	comRdCh1Sel : out std_logic_vector(1 downto 0);
		comRdCh2Sel : out std_logic_vector(1 downto 0);	 
		--selection com
		comSelection1 : out std_logic_vector(1 downto 0);
		comSelection2 : out std_logic;
		--alu com
		comOperation : out std_logic_vector(1 downto 0);	--UPDATE HERE: must be extended to 2 bits
		--data memory com
		 rd : out STD_LOGIC;
		 wr : out STD_LOGIC;
		 cs : out STD_LOGIC;	
		 -- PC com
		 NormalJump : out std_logic_vector(1 downto 0);
		 immediateValue : out std_logic_vector(15 downto 0)
	     );
end instructionDecoder;

--}} End of automatically maintained section

architecture instructionDecoder_a of instructionDecoder is	 
--UPDATE HERE: commands must be extende to 17 bits size to suport comOperation on 2 bits!
signal commands : std_logic_vector(16 downto 0);
begin
	--output connection 
	comWr <= commands(0);
	comWrSel <= commands(2 downto 1);
	comRdCh1Sel <= commands(4 downto 3);
	comRdCh2Sel <= commands(6 downto 5);
	comSelection1 <= commands(8 downto 7);
	comSelection2 <= commands(9);
	comOperation <= commands(16)&commands(10);   --UPDATE HERE: must be extended to 2 bits and all others higher commands bits must be shifted.
									-- to avoid others higher commands shift just concatenate bit 16 with bit 10 to obtain 2 bits commands
	rd <= commands(11);
	wr <= commands(12);
	cs <= commands(13);
	NormalJump <= commands(15 downto 14);  
	immediateValue <= instruction(15 downto 0);
	
	--UPDATE HERE: all commands must be updated to 17 bits size!
	process(instruction)
	begin
		if instruction(31 downto 26) = "000000" then
			case instruction(5 downto 0) is
				when "100000" => commands <= "0000000000"&instruction(17 downto 16)&instruction(22 downto 21)&instruction(12 downto 11)&'1';	--add
				when "100010" => commands <= "0000001000"&instruction(17 downto 16)&instruction(22 downto 21)&instruction(12 downto 11)&'1';	--sub
				when "101010" => commands <= "0000001010"&instruction(17 downto 16)&instruction(22 downto 21)&instruction(12 downto 11)&'1';	--slt  
				--UPDATE HERE: here implement and instruction
				when "100100" => commands <= "1000000000"&instruction(17 downto 16)&instruction(22 downto 21)&instruction(12 downto 11)&'1';   --and 
				when "100101" => commands <= "1000001000"&instruction(17 downto 16)&instruction(22 downto 21)&instruction(12 downto 11)&'1';   --or
				when others => commands <= (OTHERS=>'0');
			end case;
		else		 
			case instruction(31 downto 26) is
				when "001000" => commands <= "0000000100"&"00"&instruction(22 downto 21)&instruction(17 downto 16)&'1'; --addi 
				when "001100" => commands <= "1000000100"&"00"&instruction(22 downto 21)&instruction(17 downto 16)&'1'; -- andi
				when "001101" => commands <= "1000001100"&"00"&instruction(22 downto 21)&instruction(17 downto 16)&'1'; -- ori
				when "101011" => commands <= "0001100100"&instruction(17 downto 16)&instruction(22 downto 21)&"00"&'0'; --sw
				when "100011" => commands <= "0001010101"&"00"&instruction(22 downto 21)&instruction(17 downto 16)&'1'; --lw
				when "000100" => commands <= "0010001000"&instruction(17 downto 16)&instruction(22 downto 21)&"00"&'0'; --beq
				when "000101" => commands <= "0100001000"&instruction(17 downto 16)&instruction(22 downto 21)&"00"&'0'; --bne
				when "000010" => commands <= "01100000000000000"; --j
				when "001010" => commands <= "0000001110"&"00"&instruction(22 downto 21)&instruction(17 downto 16)&'1'; --slti
				when others => commands <= (OTHERS=>'0'); 
			end case;
		end if;	  
	end process;

end instructionDecoder_a;
