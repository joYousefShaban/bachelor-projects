-------------------------------------------------------------------------------
--
-- Title       : \register\
-- Design      : systemv1
-- Author      : Owner
-- Company     : UPIT
--
-------------------------------------------------------------------------------
--
-- File        : REGISTERSFILE_register.vhd
-- Generated   : Fri Feb  4 01:32:30 2022
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
--{entity {\register\} architecture {register_a}}

library IEEE;
use IEEE.STD_LOGIC_1164.all;

entity reg is
	 port(
	 clk : in STD_LOGIC;
	 enableWr : in std_logic;
	 paralelIn : in STD_LOGIC_VECTOR(31 downto 0);
	 paralelOut : out STD_LOGIC_VECTOR(31 downto 0)
	     );
end reg;

--}} End of automatically maintained section

architecture reg_a of reg is
begin

	process(clk,enableWr)
	begin
		if clk'event and clk='0' and enableWr = '1' then
			paralelOut <= paralelIn;
		end if;
	end process;
	

end reg_a;
