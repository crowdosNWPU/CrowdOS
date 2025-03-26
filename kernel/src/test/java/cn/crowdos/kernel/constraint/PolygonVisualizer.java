/*
 * Copyright 2019-2025 CrowdOS Group, Northwestern Polytechnical University. <https://github.com/crowdosNWPU/CrowdOS>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * imitations under the License.
 */
package cn.crowdos.kernel.constraint;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PolygonVisualizer extends Canvas {

    public List<Coordinate> polygon;
    private Color color;

    public PolygonVisualizer(List<Coordinate> polygon,Color color) {
        this.polygon = polygon;
        this.color = color;
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g2.setColor(this.color);
        int[] xPoints = new int[polygon.size()];
        int[] yPoints = new int[polygon.size()];
        for (int i = 0; i < polygon.size(); i++) {
            xPoints[i] = (int) polygon.get(i).longitude * 50;
            yPoints[i] = (int) polygon.get(i).latitude * 50;
        }
        g2.drawPolygon(xPoints, yPoints, polygon.size());
    }

    public static void main(String[] args) throws InvalidConstraintException {
        List<Coordinate> polygon2 = new ArrayList<>();
        polygon2.add(new Coordinate(0,1));
        polygon2.add(new Coordinate(1,3));
        polygon2.add(new Coordinate(2,4));
        polygon2.add(new Coordinate(4,2));
        polygon2.add(new Coordinate(2,0));

        Frame frame = new Frame();
        frame.setSize(400, 400);
        frame.add(new PolygonVisualizer(polygon2,Color.RED));
        frame.setVisible(true);
    }
}