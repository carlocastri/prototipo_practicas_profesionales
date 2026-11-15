import { DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Empresa, Estudiante, Postulacion, Vacante } from './modelos';
import { PracticasService } from './practicas.service';

type Seccion = 'empresas' | 'estudiantes' | 'vacantes' | 'postulaciones';

@Component({
  imports: [FormsModule, DatePipe],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App implements OnInit {
  private readonly servicio = inject(PracticasService);

  protected readonly secciones: { id: Seccion; nombre: string }[] = [
    { id: 'empresas', nombre: 'Empresas' },
    { id: 'estudiantes', nombre: 'Estudiantes' },
    { id: 'vacantes', nombre: 'Vacantes' },
    { id: 'postulaciones', nombre: 'Postulaciones' },
  ];
  protected readonly seccion = signal<Seccion>('empresas');

  protected readonly empresas = signal<Empresa[]>([]);
  protected readonly estudiantes = signal<Estudiante[]>([]);
  protected readonly vacantes = signal<Vacante[]>([]);
  protected readonly postulaciones = signal<Postulacion[]>([]);
  protected readonly empresasAprobadas = computed(() =>
    this.empresas().filter((empresa) => empresa.estado === 'APROBADA'),
  );

  protected readonly aviso = signal<{ tipo: 'exito' | 'error'; texto: string } | null>(null);

  protected empresa = this.empresaVacia();
  protected estudiante = this.estudianteVacio();
  protected vacante = this.vacanteVacia();
  protected postulacion = this.postulacionVacia();

  ngOnInit(): void {
    this.cargarTodo();
  }

  protected irA(seccion: Seccion): void {
    this.seccion.set(seccion);
    this.aviso.set(null);
  }

  protected registrarEmpresa(): void {
    this.servicio.registrarEmpresa(this.empresa).subscribe({
      next: (creada) => {
        this.empresa = this.empresaVacia();
        this.exito(`Empresa "${creada.razonSocial}" registrada. Queda pendiente de validación.`);
        this.cargarTodo();
      },
      error: (error: HttpErrorResponse) => this.error(error),
    });
  }

  protected aprobarEmpresa(empresa: Empresa): void {
    this.servicio.aprobarEmpresa(empresa.id).subscribe({
      next: () => {
        this.exito(`Empresa "${empresa.razonSocial}" aprobada. Ya puede publicar vacantes.`);
        this.cargarTodo();
      },
      error: (error: HttpErrorResponse) => this.error(error),
    });
  }

  protected registrarEstudiante(): void {
    this.servicio.registrarEstudiante(this.estudiante).subscribe({
      next: (creado) => {
        this.estudiante = this.estudianteVacio();
        this.exito(`Estudiante "${creado.nombre}" registrado.`);
        this.cargarTodo();
      },
      error: (error: HttpErrorResponse) => this.error(error),
    });
  }

  protected publicarVacante(): void {
    this.servicio.publicarVacante(this.vacante).subscribe({
      next: (creada) => {
        this.vacante = this.vacanteVacia();
        this.exito(`Vacante "${creada.titulo}" publicada.`);
        this.cargarTodo();
      },
      error: (error: HttpErrorResponse) => this.error(error),
    });
  }

  protected postular(): void {
    this.servicio.postular(this.postulacion).subscribe({
      next: (creada) => {
        this.postulacion = this.postulacionVacia();
        this.exito(`Postulación registrada en estado ${creada.estado}.`);
        this.cargarTodo();
      },
      error: (error: HttpErrorResponse) => this.error(error),
    });
  }

  private cargarTodo(): void {
    this.servicio.listarEmpresas().subscribe((datos) => this.empresas.set(datos));
    this.servicio.listarEstudiantes().subscribe((datos) => this.estudiantes.set(datos));
    this.servicio.listarVacantes().subscribe((datos) => this.vacantes.set(datos));
    this.servicio.listarPostulaciones().subscribe((datos) => this.postulaciones.set(datos));
  }

  private exito(texto: string): void {
    this.aviso.set({ tipo: 'exito', texto });
  }

  private error(error: HttpErrorResponse): void {
    const texto =
      error.error?.mensaje ?? 'No fue posible completar la operación. Verifique que el servidor esté activo.';
    this.aviso.set({ tipo: 'error', texto });
  }

  private empresaVacia() {
    return { nit: '', razonSocial: '', representanteLegal: '', correo: '' };
  }

  private estudianteVacio() {
    return { documento: '', nombre: '', correo: '', programa: '', semestre: 8 };
  }

  private vacanteVacia() {
    return {
      idEmpresa: null as number | null,
      titulo: '',
      descripcion: '',
      criteriosSeleccion: '',
      cupos: 1,
      modalidad: 'PRESENCIAL',
      fechaCierre: '',
    };
  }

  private postulacionVacia() {
    return { idEstudiante: null as number | null, idVacante: null as number | null, autorizaDatos: false };
  }
}
