package PacSim.Tests;


import PacSim.Game.Game;
import PacSim.Game.MapLoader;
import PacSim.Graphics.Display;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class UnitTests {
    @Test //(expected = Exception.class)
    public void AlCrearArchivoNoExistenteSeEsperaExcepcion() {
        Display.getSingleton().run();
        MapLoader.loadMap("mapnoexiste");
    }

    @Test (expected = Error.class)
    public void AlCrearArchivoConConfiguracionInvalidaSeEsperaExcepcion() {
        Display.getSingleton().run();
        MapLoader.loadMap("mapinvalido");
    }

    @Test
    public void AlCrearArchivoConConfiguracionValidoSeVerificanElTablero() {
        Display.getSingleton().run();
        MapLoader.loadMap("map1");
        int M = 7;
        int N = 10;
        int S = 13;
        int E = 64;

        Assert.assertEquals(M, Game.tileMap.getDimensionM());
        Assert.assertEquals(N, Game.tileMap.getDimensionN());
        Assert.assertEquals(S, Game.finalPos);
        //Entrada no está en Game.
    }

    @Test
    public void AlCrearArchivoConConfiguracionValidoSeVerificanLosBloqueos() {
        Display.getSingleton().run();
        MapLoader.loadMap("map1");
        String bloqs = "1111111000000000000110000001111110111100010000011110011100000011011111";

        for (int i = 0; i < Game.tileMap.size(); ++i) {
            Assert.assertEquals(Game.tileMap.getTile(i).isBlocked(), bloqs.charAt(i) == '1');
        }
    }

    @Test
    public void AlCrearArchivoConConfiguracionValidoSeVerificanLasPosicionesDeLasMinas() {
        Display.getSingleton().run();
        MapLoader.loadMap("map1");
        String bloqs = "11,16,17,18,42,43,44,51,56,57,58";

        bloqs = bloqs.replace(" ", "");
        List<String> dataMinas = Arrays.asList(bloqs.split(","));

        for (int i = 0; i < dataMinas.size(); ++i) {
            final Integer posMine = Integer.parseInt(dataMinas.get(i));
            Assert.assertEquals(Game.tileMap.getTile(posMine).getObjects().contains(Game.objData.get("B")), true);
        }
    }

    @Test
    public void AlCrearArchivoConConfiguracionValidoSeVerificanLasPosicionesDeLasProvisiones() {
        Display.getSingleton().run();
        MapLoader.loadMap("map1");
        int P1 = 7;
        int P2 = 14;
        int P6 = 64;

        Assert.assertEquals(Game.tileMap.getTile(P1).getObjects().contains(Game.objData.get("P1")), true);
        Assert.assertEquals(Game.tileMap.getTile(P2).getObjects().contains(Game.objData.get("P2")), true);
        Assert.assertEquals(Game.tileMap.getTile(P6).getObjects().contains(Game.objData.get("P6")), true);
    }

    @Test (expected = Exception.class)
    public void AlCrearArchivoConConfiguracionValidoSeVerificanQueUnaPosicionInvalidaLanzaExcepcionEnUnaProvision() {
        Display.getSingleton().run();
        MapLoader.loadMap("map1");
        int P6 = 63;
        Assert.assertEquals(Game.tileMap.getTile(P6).getObjects().contains(Game.objData.get("P6")), true);
    }

    @Test
    public void SeVerificaQueElUsuarioEsteEfectivamenteEnLaPosicionInicial() {
        Display.getSingleton().run();
        MapLoader.loadMap("map1");
        int initPos = 64;
        Assert.assertEquals(Game.player.getPosY() * Game.tileMap.getDimensionM() + Game.player.getPosX(), initPos);
    }

    @Test
    public void SeVerificaQueElUsuarioFinalizaElJuegoEnLaPosicionFinal() {
        Display.getSingleton().run();
        MapLoader.loadMap("map1");
        int finalPos = 13;

        Game.player.setPosition(6, 1);
        Game.finishGame = true;
        int posActual = Game.player.getPosY() * Game.tileMap.getDimensionM() + Game.player.getPosX();

        Assert.assertEquals(finalPos, posActual);
        Assert.assertEquals(true, Game.finishGame);
    }

    @Test
    public void SeVerificaQueElUsuarioPerdioCuandoSeQuedoSinVidas() {
        Display.getSingleton().run();
        MapLoader.loadMap("map1");
        int finalPos = 13;
        int posActual = Game.player.getPosY() * Game.tileMap.getDimensionM() + Game.player.getPosX();

        Assert.assertEquals(true, Game.finishGame);
        Assert.assertNotEquals(finalPos, posActual);
    }
}
