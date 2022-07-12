-------------------------------------------------------------------------------
--
-- Title       : programCounter
-- Design      : systemv1
-- Author      : Owner
-- Company     : UPIT
--
-------------------------------------------------------------------------------
--
-- File        : PC.vhd
-- Generated   : Fri Feb  4 02:19:21 2022
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
--{entity {programCounter} architecture {programCounter_a}}

library IEEE;
use IEEE.STD_LOGIC_1164.all;							 
use IEEE.STD_LOGIC_UNSIGNED.all;

entity programCounter is
	 port(
		 clk : in STD_LOGIC;
		 reset : in STD_LOGIC;
		 NormalJump : in std_logic_vector(1 downto 0);
		 Z : in STD_LOGIC;
		 jumpAddress: std_logic_vector(7 downto 0);
		 instructionAddress : out STD_LOGIC_VECTOR(7 downto 0)
	     );
end programCounter;

--}} End of automatically maintained section

architecture programCounter_a of programCounter is
begin
	
	process(clk,reset,NormalJump,Z)
	variable address : std_logic_vector(7 downto 0);
	begin	
		if clk'event and clk='1' then
			if reset = '1' then
				address := (OTHERS=>'0');
			else
				case NormalJump is
					when "00" => address:= address + '1';
					when "01" => if Z = '1' then address := jumpAddress+address; 
								end if;											 
								address := address + '1';
					when "10" => if Z = '0' then address := jumpAddress+address;
								end if;	   
								address := address + '1';
					when "11" => address := jumpAddress;
					when others => address := (OTHERS=>'0');
				end case;
			end if;
		end if;
		 instructionAddress <= address;
	end process;

end programCounter_a;
