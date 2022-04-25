package PacSim.Game;

import PacSim.Graphics.Display;
import PacSim.Graphics.Tile;
import PacSim.Graphics.TileMap;
import org.ini4j.Wini;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapLoader {
    public static void loadMap(String file) {
        try {
            Wini ini = new Wini(MapLoader.class.getClassLoader().getResource("maps/" + file + ".slark"));
            Game.objData.put("B", new Mine());

            int m = ini.get("TABLERO", "M", int.class);
            int n = ini.get("TABLERO", "N", int.class);
            int initPos = ini.get("TABLERO", "E", int.class);
            int targetPos = ini.get("TABLERO", "S", int.class);
            String tempBloqueos = ini.get("TABLERO", "L", String.class);
            String tempMinas = ini.get("MINAS", "B", String.class);

            int sizeTiles = m * n;

            if (initPos < 0 || initPos >= sizeTiles)
                throw new Error("Mapa inválido. Posición de entrada no corresponde a una posición válida.");

            if (targetPos < 0 || targetPos >= sizeTiles)
                throw new Error("Mapa inválido. Posición de salida no corresponde a una posición válida.");

            if (tempBloqueos.length() != sizeTiles)
                throw new Error("Mapa inválido. Bloqueos generan overflow.");

            List<Tile> tileMap = new ArrayList<>(m * n);
            for (int i = 0; i < m * n; ++i) {
                tileMap.add(i, new Tile());
            }

            for (int i = 0; i < tempBloqueos.length(); ++i) {
                tileMap.get(i).setBlocked(tempBloqueos.charAt(i) == '1');
            }

            tempMinas = tempMinas.replace(" ", "");
            List<String> dataMinas = Arrays.asList(tempMinas.split(","));

            for (int i = 0; i < dataMinas.size(); ++i) {
                final Integer posMine = Integer.parseInt(dataMinas.get(i));
                if (posMine < 0 || posMine >= sizeTiles)
                    throw new Error("Mapa inválido. Minas generan overflow.");

                tileMap.get(posMine).setObject(Game.objData.get("B"));
            }

            //PROVISIONES
            int sinceProv = 1;
            while (ini.get("PROVISIONES", "P" + sinceProv, String.class) != null)
            {
                String pName = "P" + sinceProv;
                String tempProvisiones = ini.get("PROVISIONES", pName, String.class);
                tempProvisiones = tempProvisiones.replace(" ", "");
                List<String> dataProvisiones = Arrays.asList(tempProvisiones.split(","));
                Game.objData.put(pName, new Obj());

                for (int i = 0; i < dataProvisiones.size(); ++i) {
                    String tempDataProv = dataProvisiones.get(i);

                    if (tempDataProv.equalsIgnoreCase("E")) {
                        Game.objData.get(pName).add(new Provision(1, 0));
                    }
                    else if (tempDataProv.equalsIgnoreCase("V")) {
                        Game.objData.get(pName).add(new Provision(0, 1));
                    }
                    else if (Game.objData.containsKey(tempDataProv)) {
                        Game.objData.get(pName).add(Game.objData.get(tempDataProv));
                    }
                }

                ++sinceProv;
            }

            ///////////UBICACION_PROVISIONES
            sinceProv = 1;
            while (ini.get("UBICACION_PROVISIONES", "P" + sinceProv, String.class) != null)
            {
                String pName = "P" + sinceProv;
                String tempUbicacionProvisiones = ini.get("UBICACION_PROVISIONES", pName, String.class);
                tempUbicacionProvisiones = tempUbicacionProvisiones.replace(" ", "");
                List<String> dataUbicacionProvisiones = Arrays.asList(tempUbicacionProvisiones.split(","));

                for (int i = 0; i < dataUbicacionProvisiones.size(); ++i) {
                    Integer tempPos = Integer.parseInt(dataUbicacionProvisiones.get(i));

                    if (tempPos < 0 || tempPos >= sizeTiles)
                        throw new Error("Mapa inválido. Posición de una Provisión genera overflow.");

                    tileMap.get(tempPos).setObject(Game.objData.get(pName));
                }

                ++sinceProv;
            }

            Display.resizeWindow(m * 32, n * 32);
            Game.tileMap = new TileMap(tileMap, m, n);
            Game.player = new Player(initPos);
            Game.finalPos = targetPos;
            ini.clear();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
