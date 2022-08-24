-------------------------------------------------------------------------------
--
-- Title       : REGISTERFILE
-- Design      : systemv1
-- Author      : Owner
-- Company     : UPIT
--
-------------------------------------------------------------------------------
--
-- File        : REGISTERFILE.vhd
-- Generated   : Fri Feb  4 01:35:11 2022
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
--{entity {REGISTERFILE} architecture {REGISTERFILE_a}}

library IEEE;
use IEEE.STD_LOGIC_1164.all;

entity REGISTERFILE is 
	 port(
		 clk : in STD_LOGIC;
		 comWr : in STD_LOGIC;
		 DatWr : in STD_LOGIC_VECTOR(31 downto 0);
		 comWrSel : in STD_LOGIC_VECTOR(1 downto 0);  
		 comRdCh1Sel : in std_logic_vector(1 downto 0);
		 comRdCh2Sel : in std_logic_vector(1 downto 0);
		 DatRdCh1 : out STD_LOGIC_VECTOR(31 downto 0);
		 DatRdCh2 : out STD_LOGIC_VECTOR(31 downto 0)
	     );
end REGISTERFILE;

--}} End of automatically maintained section

architecture REGISTERFILE_a of REGISTERFILE is	   
--declaration zone
--components
component reg is
	 port(
	 clk : in STD_LOGIC;
	 enableWr : in std_logic;
	 paralelIn : in STD_LOGIC_VECTOR(31 downto 0);
	 paralelOut : out STD_LOGIC_VECTOR(31 downto 0)
	     );
end component;

--signals
signal dmuxOut: std_logic_vector(3 downto 0);
signal registersOutputs : std_logic_vector(4*32-1 downto 0);


begin
	--registers
	regs: for i in 1 to 3 generate
		creg: reg port map(clk, dmuxOut(i), DatWr,registersOutputs((i+1)*32-1 downto i*32));
	end generate;
	
	--clocks demultiplexer
	with comWrSel select
	dmuxOut <= "0001" when "00",
	"0010" when "01",
	"0100" when "10",
	"1000" when "11",
	"0000" when others;

	
	--multiplexer for  DatRdCh1
	with comRdCh1Sel select
	DatRdCh1 <= --registersOutputs((1*32)-1 downto 0*32) when "00",
	registersOutputs((2*32)-1 downto 1*32) when "01",
	registersOutputs((3*32)-1 downto 2*32) when "10",   
	registersOutputs((4*32)-1 downto 3*32) when "11", 
	(OTHERS=>'0') when others;
	
	--multiplexer for  DatRdCh2
	with comRdCh2Sel select
	DatRdCh2 <= --registersOutputs((1*32)-1 downto 0*32) when "00",
	registersOutputs((2*32)-1 downto 1*32) when "01",
	registersOutputs((3*32)-1 downto 2*32) when "10",   
	registersOutputs((4*32)-1 downto 3*32) when "11", 
	(OTHERS=>'0') when others;	

end REGISTERFILE_a;
