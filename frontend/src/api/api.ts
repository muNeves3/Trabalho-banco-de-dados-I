const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

type Usuario = {
  cpf: string;
  email: string;
  nome?: string;
};

type LoginPayload = {
  email: string;
  senhaHash: string;
};

type CadastroPayload = {
  cpf: string;
  email: string;
  senhaHash: string;
  nome: string;
};

async function parseError(response: Response, fallbackMessage: string) {
  const backendMessage = await response.text();
  throw new Error(backendMessage || fallbackMessage);
}

export async function buscarUsuarioPorCpf(cpf: string): Promise<Usuario> {
  const response = await fetch(`${API_BASE_URL}/api/usuarios/${cpf}`);

  if (!response.ok) {
    throw new Error('CPF não encontrado.');
  }

  return response.json();
}

export async function loginUsuario(payload: LoginPayload): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/api/usuarios/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  });

  if (!response.ok) {
    await parseError(response, 'Falha no login.');
  }
}

export async function cadastrarUsuario(payload: CadastroPayload): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/api/usuarios`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  });

  if (!response.ok) {
    await parseError(response, 'Falha no cadastro.');
  }
}

export async function listarUsuarios(): Promise<Usuario[]> {
  const response = await fetch(`${API_BASE_URL}/api/usuarios`);

  if (!response.ok) {
    throw new Error('Erro ao buscar usuários.');
  }

  return response.json();
}

export async function deletarUsuario(cpf: string): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/api/usuarios/${cpf}`, {
    method: 'DELETE'
  });

  if (!response.ok) {
    await parseError(response, 'Erro ao deletar usuário.');
  }
}

export async function buscarPorId(id: number): Promise<Usuario> {
  const response = await fetch(`${API_BASE_URL}/api/usuarios/id/${id}`);

  if (!response.ok) {
    throw new Error('Usuário não encontrado.');
  }

  return response.json();
}

export async function atualizarUsuario(cpf: string, payload: Partial<CadastroPayload>): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/api/usuarios/${cpf}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  });

  if (!response.ok) {
    await parseError(response, 'Erro ao atualizar usuário.');
  }
}

export async function deletarUsuarioPorId(id: number): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/api/usuarios/id/${id}`, {
    method: 'DELETE'
  });

  if (!response.ok) {
    await parseError(response, 'Erro ao deletar usuário.');
  }
}

export async function authenticateUsuario(payload: LoginPayload): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/api/usuarios/authenticate`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  });

  if (!response.ok) {
    await parseError(response, 'Falha ao autenticar.');
  }
}

// tipos e funções para DATASETS

export type Dataset = {
  id: number;
  nome: string;
  descricao: string;
  fontes: string;
  criadorCpf: string;
  criadoEm: string;
};

export type DatasetPayload = {
  nome: string;
  descricao: string;
  fontes: string;
  criadorCpf: string;
};

export async function cadastrarDataset(payload: DatasetPayload): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/api/datasets`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  });

  if (!response.ok) {
    await parseError(response, 'Falha ao cadastrar dataset.');
  }
}

export async function listarDatasets(): Promise<Dataset[]> {
  const response = await fetch(`${API_BASE_URL}/api/datasets`);

  if (!response.ok) {
    throw new Error('Erro ao buscar datasets.');
  }

  return response.json();
}

export async function baixarDataset(id: number): Promise<Blob> {
  const response = await fetch(`${API_BASE_URL}/api/datasets/${id}/download`);

  if (!response.ok) {
    await parseError(response, 'Erro ao baixar dataset.');
  }

  return response.blob();
}


// tipos e funções para ACESSO VERSAO

export type AcessoVersaoRequest = {
  usuarioCpf: string;
  datasetId: number;
  numeroVersao: number;
  tipoAcesso: string; 
};

export type AcessoVersaoResponse = {
  usuarioCpf: string;
  datasetId: number;
  numeroVersao: number;
  tipoAcesso: string;
  acessadoEm: string;
};

export async function registrarAcesso(payload: AcessoVersaoRequest): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/api/acessos-versao`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  });

  if (!response.ok) {
    await parseError(response, 'Falha ao registrar acesso.');
  }
}

export async function listarAcessos(): Promise<AcessoVersaoResponse[]> {
  const response = await fetch(`${API_BASE_URL}/api/acessos-versao`);
  if (!response.ok) throw new Error('Erro ao buscar todos os acessos.');
  return response.json();
}

export async function listarAcessosPorUsuario(cpf: string): Promise<AcessoVersaoResponse[]> {
  const response = await fetch(`${API_BASE_URL}/api/acessos-versao/usuario/${cpf}`);
  if (!response.ok) throw new Error('Erro ao buscar acessos do usuário.');
  return response.json();
}

export async function listarAcessosPorDataset(datasetId: number): Promise<AcessoVersaoResponse[]> {
  const response = await fetch(`${API_BASE_URL}/api/acessos-versao/dataset/${datasetId}`);
  if (!response.ok) throw new Error('Erro ao buscar acessos do dataset.');
  return response.json();
}

export type VersaoDataset = {
  datasetId: number;
  numeroVersao: number;
  versaoBaseNumero: number | null;
  criadorCpf: string;
  descModificacoes: string;
  criadoEm: string;
};

export type NovaVersaoPayload = {
  datasetId: number;
  versaoBaseNumero: number | null;
  criadorCpf: string;
  descModificacoes: string;
  arquivo: string;
  numeroVersao?: number;
};

export async function listarVersoesDataset(datasetId: number): Promise<VersaoDataset[]> {
  const response = await fetch(`${API_BASE_URL}/api/versoes/${datasetId}`);
  if (!response.ok) {
    await parseError(response, 'Erro ao buscar versões do dataset.');
  }
  return response.json();
}

export async function baixarVersaoDataset(datasetId: number, numeroVersao: number): Promise<Blob> {
  const response = await fetch(`${API_BASE_URL}/api/versoes/${datasetId}/${numeroVersao}/download`);
  if (!response.ok) {
    await parseError(response, 'Erro ao baixar versão do dataset.');
  }
  return response.blob();
}

export async function visualizarVersaoDataset(datasetId: number, numeroVersao: number): Promise<VersaoDataset> {
  const response = await fetch(`${API_BASE_URL}/api/versoes/${datasetId}/${numeroVersao}`);
  if (!response.ok) {
    await parseError(response, 'Erro ao visualizar versão do dataset.');
  }
  return response.json();
}

export async function cadastrarNovaVersaoDataset(payload: NovaVersaoPayload): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/api/versoes`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  });

  if (!response.ok) {
    await parseError(response, 'Falha ao cadastrar nova versão.');
  }
}