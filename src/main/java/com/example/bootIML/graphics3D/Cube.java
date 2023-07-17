package com.example.bootIML.graphics3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Cube {
    public List<Triangle> tris = new ArrayList<>();
    public Cube (int edgeOfTheCube){

        //A
        tris.add(new Triangle(new Vertex(-edgeOfTheCube, edgeOfTheCube, edgeOfTheCube, 1),
                new Vertex(edgeOfTheCube, edgeOfTheCube, edgeOfTheCube, 1),
                new Vertex(-edgeOfTheCube, edgeOfTheCube, -edgeOfTheCube, 1),
                Color.WHITE));
        //B
        tris.add(new Triangle(new Vertex(edgeOfTheCube, edgeOfTheCube, edgeOfTheCube, 1),
                new Vertex(edgeOfTheCube, edgeOfTheCube, -edgeOfTheCube, 1),
                new Vertex(-edgeOfTheCube, edgeOfTheCube, -edgeOfTheCube, 1),
                Color.WHITE));
        //C
        tris.add(new Triangle(new Vertex(edgeOfTheCube, -edgeOfTheCube, edgeOfTheCube, 1),
                new Vertex(edgeOfTheCube, edgeOfTheCube, -edgeOfTheCube, 1),
                new Vertex(edgeOfTheCube, edgeOfTheCube, edgeOfTheCube, 1),
                Color.WHITE));
        //D
        tris.add(new Triangle(new Vertex(edgeOfTheCube, -edgeOfTheCube, edgeOfTheCube, 1),
                new Vertex(edgeOfTheCube, -edgeOfTheCube, -edgeOfTheCube, 1),
                new Vertex(edgeOfTheCube, edgeOfTheCube, -edgeOfTheCube, 1),
                Color.WHITE));
        //E
        tris.add(new Triangle(new Vertex(-edgeOfTheCube, -edgeOfTheCube, edgeOfTheCube, 1),
                new Vertex(edgeOfTheCube, -edgeOfTheCube, edgeOfTheCube, 1),
                new Vertex(-edgeOfTheCube, edgeOfTheCube, edgeOfTheCube, 1),
                Color.WHITE));

        //F
        tris.add(new Triangle(new Vertex(edgeOfTheCube, -edgeOfTheCube, edgeOfTheCube, 1),
                new Vertex(edgeOfTheCube, edgeOfTheCube, edgeOfTheCube, 1),
                new Vertex(-edgeOfTheCube, edgeOfTheCube, edgeOfTheCube, 1),
                Color.WHITE));
        //G
        tris.add(new Triangle(new Vertex(-edgeOfTheCube, -edgeOfTheCube, edgeOfTheCube, 1),
                new Vertex(-edgeOfTheCube, edgeOfTheCube, edgeOfTheCube, 1),
                new Vertex(-edgeOfTheCube, -edgeOfTheCube, -edgeOfTheCube, 1),
                Color.WHITE));
        //H
        tris.add(new Triangle(new Vertex(-edgeOfTheCube, edgeOfTheCube, edgeOfTheCube, 1),
                new Vertex(-edgeOfTheCube, edgeOfTheCube, -edgeOfTheCube, 1),
                new Vertex(-edgeOfTheCube, -edgeOfTheCube, -edgeOfTheCube, 1),
                Color.WHITE));
        //I
        tris.add(new Triangle(new Vertex(-edgeOfTheCube, edgeOfTheCube, -edgeOfTheCube, 1),
                new Vertex(edgeOfTheCube, edgeOfTheCube, -edgeOfTheCube, 1),
                new Vertex(-edgeOfTheCube, -edgeOfTheCube, -edgeOfTheCube, 1),
                Color.WHITE));
        //J
        tris.add(new Triangle(new Vertex(-edgeOfTheCube, -edgeOfTheCube, -edgeOfTheCube, 1),
                new Vertex(edgeOfTheCube, edgeOfTheCube, -edgeOfTheCube, 1),
                new Vertex(edgeOfTheCube, -edgeOfTheCube, -edgeOfTheCube, 1),
                Color.WHITE));
        //K
        tris.add(new Triangle(new Vertex(edgeOfTheCube, -edgeOfTheCube, edgeOfTheCube, 1),
                new Vertex(-edgeOfTheCube, -edgeOfTheCube, edgeOfTheCube, 1),
                new Vertex(-edgeOfTheCube, -edgeOfTheCube, -edgeOfTheCube, 1),
                Color.WHITE));
        //L
        tris.add(new Triangle(new Vertex(-edgeOfTheCube, -edgeOfTheCube, -edgeOfTheCube, 1),
                new Vertex(edgeOfTheCube, -edgeOfTheCube, -edgeOfTheCube, 1),
                new Vertex(edgeOfTheCube, -edgeOfTheCube, edgeOfTheCube, 1),
                Color.WHITE));

    }
}
