import { Estanteria } from "./estanteria";

export interface Libro {
    id: number,
    nombre: string,
    autor: string,
    ubicacion: Estanteria
}