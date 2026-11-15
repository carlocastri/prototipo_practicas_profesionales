export interface Empresa {
  id: number;
  nit: string;
  razonSocial: string;
  representanteLegal: string;
  correo: string;
  estado: 'PENDIENTE_VALIDACION' | 'APROBADA';
}

export interface Estudiante {
  id: number;
  documento: string;
  nombre: string;
  correo: string;
  programa: string;
  semestre: number;
}

export interface Vacante {
  id: number;
  empresa: Empresa;
  titulo: string;
  descripcion: string;
  criteriosSeleccion: string;
  cupos: number;
  modalidad: string;
  fechaCierre: string;
  estado: string;
}

export interface Postulacion {
  id: number;
  estudiante: Estudiante;
  vacante: Vacante;
  estado: string;
  autorizaDatos: boolean;
  versionPolitica: string;
  fechaPostulacion: string;
}
