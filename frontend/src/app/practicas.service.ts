import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Empresa, Estudiante, Postulacion, Vacante } from './modelos';

/** Cliente de la API REST del backend (Spring Boot). */
@Injectable({ providedIn: 'root' })
export class PracticasService {
  private readonly http = inject(HttpClient);
  private readonly api = '/api';

  listarEmpresas(): Observable<Empresa[]> {
    return this.http.get<Empresa[]>(`${this.api}/empresas`);
  }

  registrarEmpresa(datos: Partial<Empresa>): Observable<Empresa> {
    return this.http.post<Empresa>(`${this.api}/empresas`, datos);
  }

  aprobarEmpresa(id: number): Observable<Empresa> {
    return this.http.put<Empresa>(`${this.api}/empresas/${id}/aprobar`, {});
  }

  listarEstudiantes(): Observable<Estudiante[]> {
    return this.http.get<Estudiante[]>(`${this.api}/estudiantes`);
  }

  registrarEstudiante(datos: Partial<Estudiante>): Observable<Estudiante> {
    return this.http.post<Estudiante>(`${this.api}/estudiantes`, datos);
  }

  listarVacantes(): Observable<Vacante[]> {
    return this.http.get<Vacante[]>(`${this.api}/vacantes`);
  }

  publicarVacante(datos: object): Observable<Vacante> {
    return this.http.post<Vacante>(`${this.api}/vacantes`, datos);
  }

  listarPostulaciones(): Observable<Postulacion[]> {
    return this.http.get<Postulacion[]>(`${this.api}/postulaciones`);
  }

  postular(datos: object): Observable<Postulacion> {
    return this.http.post<Postulacion>(`${this.api}/postulaciones`, datos);
  }
}
