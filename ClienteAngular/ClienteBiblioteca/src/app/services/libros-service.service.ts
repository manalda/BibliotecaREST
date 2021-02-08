import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { MessageService } from './message.service';
import { Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Libro } from '../model/libro';
import { environment } from 'src/environments/environment';
import { Estanteria } from '../model/estanteria';

@Injectable({
  providedIn: 'root'
})
export class LibrosServiceService {
  private readonly END_POINT: string = 'libros'
  private readonly SEARCH_BY_NAME: string = 'libros?nombre='
  private readonly SEARCH_BY_AUTOR: string = 'libros?autor='
  private readonly SEARCH_BY_UBICACION: string = 'libros?lugar='

  private httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  }

  constructor(private http: HttpClient, private messageService: MessageService) { }

  /**
   * GET: obtiene todos los libros
   */
  getAllLibros(): Observable<Libro[]> {
    const url = `${environment.url}${this.END_POINT}`

    return this.http.get<Libro[]>(url).pipe(
      tap(_ => this.logMessage("Libros leídos correctamente")),
      catchError(this.handleError<Libro[]>("getAllLibros", []))
    )
  }

  /**
   * GET: obtiene un libro a partir de su id
   * 
   * @param id el id del libro que se quiere leer 
   */
  getLibroById(id: number): Observable<Libro> {
    const url = `${environment.url}${this.END_POINT}/${id}`

    return this.http.get<Libro>(url).pipe(
      tap(_ => this.logMessage(`Leído el libro con id: ${id}`)),
      catchError(this.handleError<Libro>('getLibroById'))
    )
  }

  /**
   * POST: crea un libro a partir de sus datos
   */
  createLibro(libro: Libro): Observable<Libro> {
    const url = `${environment.url}${this.END_POINT}`

    return this.http.post<Libro>(url, libro, this.httpOptions).pipe(
      tap((newLibro: Libro) => this.logMessage(`Libro ${newLibro.nombre} creado`)),
      catchError(this.handleError<Libro>('createLibro'))
    )
  }

  /**
   * PUT: actualiza los datos de un libro un libro
   */
  updateLibro(libro: Libro): Observable<any> {
    const url = `${environment.url}${this.END_POINT}/${libro.id}`

    return this.http.put(url, libro, this.httpOptions).pipe(
      tap(_ => this.logMessage(`actualizado el libro ${libro.id}`)),
      catchError(this.handleError<any>('updateLibro'))
    )
  }

  /** 
   * DELETE: elimina un libro 
   */
  deleteLibro(libro: Libro): Observable<Libro> {
    const url = `${environment.url}${this.END_POINT}/${libro.id}`

    return this.http.delete<Libro>(url, this.httpOptions).pipe(
      tap(_ => this.logMessage(`Libro ${libro.nombre} borrado con éxito`)),
      catchError(this.handleError<Libro>('deleteLibro'))
    )
  }
  
  /**
   * GET: obtiene un libro a partir de su nombre
   * 
   * @param term el término de búsqueda
   */
  searchByNombre(term: string): Observable<Libro[]> {
    if(!term.trim()) {
      return of([])
    }

    const url = `${environment.url}${this.SEARCH_BY_NAME}${term}`

    return this.http.get<Libro[]>(url).pipe(
      tap(x => this.logMessage(`Hay ${x.length} coincidencias con el término: ${term}`)),
      catchError(this.handleError<Libro[]>('searchByNombre', []))
    )
  }

  /**
   * GET: obtiene un libro a partir de su autor
   * 
   * @param term el término de búsqueda
   */
  searchByAutor(term: string): Observable <Libro[]> {
    if(!term.trim()) {
      return of([])
    }

    const url = `${environment.url}${this.SEARCH_BY_AUTOR}${term}`

    return this.http.get<Libro[]>(url).pipe(
      tap(x => this.logMessage(`Hay ${x.length} coincidencias con el término: ${term}`)),
      catchError(this.handleError<Libro[]>('searchByAutor', []))
    )
  }

  /**
   * GET: obtiene un libro a partir de su ubicación
   * 
   * @param lugar la estantería en la que se quiere buscar
   */
  searchByLugar(lugar: Estanteria): Observable<Libro[]> {
    const url = `${environment.url}${this.SEARCH_BY_AUTOR}${lugar.id}`

    return this.http.get<Libro[]>(url).pipe(
      tap(x => this.logMessage(`Hay ${x.length} coincidencias con la ubicación: ${lugar.nombre}`)),
      catchError(this.handleError<Libro[]>('searchByAutor', []))
    )
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      const error_message = `Error. Operation: ${operation}`

      console.log(error_message)
      this.logMessage(error_message)

      return of(result as T)
    }
  }

  private logMessage(message: string) {
    this.messageService.add(message)
  }
}
