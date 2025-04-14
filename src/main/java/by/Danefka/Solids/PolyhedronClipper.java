package by.Danefka.Solids;

import java.util.ArrayList;

import java.util.*;
import java.util.stream.Collectors;

public class PolyhedronClipper {
    private static class Pair<A, B> {
        final A first;
        final B second;

        Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair<?, ?> pair = (Pair<?, ?>) o;
            return Objects.equals(first, pair.first) &&
                   Objects.equals(second, pair.second);
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }
    }

    public static Polyhedron clipPolyhedron(Polyhedron polyhedron) {
        double[][] vertices = polyhedron.getVertices();
        ArrayList<double[]> newVertices = new ArrayList<>();
        newVertices.addAll(Arrays.asList(vertices));

        HashMap<Pair<Integer, Integer>, Integer> replacedEdges = new HashMap<>();
        ArrayList<Integer> verticesAtZero = new ArrayList<>();
        ArrayList<Face> newFaces = new ArrayList<>();

        // Обработка исходных граней
        for (Face face : polyhedron.getFaces()) {
            ArrayList<Integer> newFace = new ArrayList<>();
            int[] facesVertices = face.getVerts();

            for (int i = 0; i < face.getNumVerts(); i++) {
                int currentIdx = facesVertices[i % face.getNumVerts()];
                int prevIdx = facesVertices[(face.getNumVerts() + i - 1) % face.getNumVerts()];
                int nextIdx = facesVertices[(face.getNumVerts() + i + 1) % face.getNumVerts()];


                if (vertices[currentIdx][2] <= 0) {
                    newFace.add(currentIdx);
                    if ( vertices[currentIdx][2] == 0 && !verticesAtZero.contains(currentIdx)){
                        verticesAtZero.add(currentIdx);
                    }
                } else {
                    if (vertices[prevIdx][2] < 0) {
                        Pair<Integer, Integer> edge = new Pair<>(prevIdx, currentIdx);

                        if (replacedEdges.containsKey(edge)) {
                            newFace.add(replacedEdges.get(edge));
                        } else {
                            double[] v1 = vertices[prevIdx];
                            double[] v2 = vertices[currentIdx];
                            double t = -v1[2] / (v2[2] - v1[2]);

                            double x = v1[0] + t * (v2[0] - v1[0]);
                            double y = v1[1] + t * (v2[1] - v1[1]);
                            double[] newPoint = {x, y, 0};

                            newVertices.add(newPoint);
                            int newIndex = newVertices.size() - 1;
                            replacedEdges.put(edge, newIndex);
                            replacedEdges.put(new Pair<>(currentIdx, prevIdx), newIndex);
                            verticesAtZero.add(newIndex);
                            newFace.add(newIndex);
                        }
                    }
                    if (vertices[nextIdx][2] < 0) {
                        Pair<Integer, Integer> edge = new Pair<>(nextIdx, currentIdx);


                        if (replacedEdges.containsKey(edge)) {
                            newFace.add(replacedEdges.get(edge));
                        } else {
                            double[] v1 = vertices[nextIdx];
                            double[] v2 = vertices[currentIdx];
                            double t = -v1[2] / (v2[2] - v1[2]);

                            double x = v1[0] + t * (v2[0] - v1[0]);
                            double y = v1[1] + t * (v2[1] - v1[1]);
                            double[] newPoint = {x, y, 0};

                            newVertices.add(newPoint);
                            int newIndex = newVertices.size() - 1;
                            replacedEdges.put(edge, newIndex);
                            replacedEdges.put(new Pair<>(currentIdx, nextIdx), newIndex);
                            verticesAtZero.add(newIndex);
                            newFace.add(newIndex);
                        }
                    }
                }
            }

            if (newFace.size() > 2) {
                int[] vertexIndices = newFace.stream()
                        .mapToInt(Integer::intValue)
                        .toArray();
                newFaces.add(new Face(
                        vertexIndices.length,
                        face.getNorm(),
                        face.getW(),
                        vertexIndices
                ));
            }
        }

        // Добавляем новую грань на плоскости z=0
        if (verticesAtZero.size() > 2 && newVertices.size() > 1) {
            List<Integer> convexHull = convexHull(newVertices, verticesAtZero);
            if (convexHull.size() >= 3) {
                int[] hullVerts = convexHull.stream()
                        .mapToInt(Integer::intValue)
                        .toArray();
                double[] normal = {0, 0, 1};
                newFaces.add(new Face(
                        hullVerts.length,
                        normal,
                        0.0,
                        hullVerts
                ));
            }
        }

        return new Polyhedron(
                newVertices.toArray(new double[0][]),
                newFaces.toArray(new Face[0]),
                polyhedron.getDensity()
        );
    }

    private static List<Integer> convexHull(ArrayList<double[]> vertices,
                                            ArrayList<Integer> indices) {
        List<Point2D> points = indices.stream()
                .map(idx -> new Point2D(vertices.get(idx)[0], vertices.get(idx)[1]))
                .collect(Collectors.toList());

        return grahamScan(points).stream()
                .map(p -> findOriginalIndex(vertices, p))
                .collect(Collectors.toList());
    }

    private static double ccw(Point2D a, Point2D b, Point2D c) {
        return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x);
    }

    // Реализация алгоритма Грэхема для 2D
    private static List<Point2D> grahamScan(List<Point2D> points) {
        if (points.size() < 3) return points;

        // Находим опорную точку (pivot)
        Point2D pivot = points.stream()
                .min((a, b) -> a.y != b.y ? Double.compare(a.y, b.y) : Double.compare(a.x, b.x))
                .get();

        // Сортируем по полярному углу
        List<Point2D> sorted = new ArrayList<>(points);
        sorted.sort((a, b) -> {
            if (a == pivot) return -1;
            if (b == pivot) return 1;
            double angleA = Math.atan2(a.y - pivot.y, a.x - pivot.x);
            double angleB = Math.atan2(b.y - pivot.y, b.x - pivot.x);
            return Double.compare(angleA, angleB);
        });

        // Алгоритм Грэхема с обработкой коллинеарных точек
        Stack<Point2D> stack = new Stack<>();
        stack.push(pivot);
        stack.push(sorted.get(0));

        for (int i = 1; i < sorted.size(); i++) {
            Point2D top = stack.pop();
            // Удаляем точки, пока не получим левый поворот
            while (!stack.isEmpty() && ccw(stack.peek(), top, sorted.get(i)) <= 0) {
                top = stack.pop();
            }
            stack.push(top);
            stack.push(sorted.get(i));
        }

        return new ArrayList<>(stack);
    }

    private static class Point2D {
        final double x, y;

        Point2D(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private static int findOriginalIndex(ArrayList<double[]> vertices, Point2D p) {
        for (int i = 0; i < vertices.size(); i++) {
            double[] v = vertices.get(i);
            if (Math.abs(v[0] - p.x) < 1e-9 &&
                Math.abs(v[1] - p.y) < 1e-9 &&
                v[2] == 0) {
                return i;
            }
        }
        return -1;
    }
}