

interface ErrorDto {
  statusCode: number;
  message: string;
  error: string;
}

interface LoginDto {
  username: string;
  password: string;
}

type NoteLevel = 'INITIAL' | 'PREGRUADATED' | 'GRADUATED' | 'NEXT';
type Optionote = 'BIEN' | 'DIFICIL';

interface OptionDto {
  idnote: number;
  option: Optionote;
}

interface NotesSummary {
  newnotes: number;
  goneovernotes: number;
  graduatednotes: number;
}

interface IFlashcard {
  id: number;
  front: string;
  back: string;
  note: string;
  createdAt: string;      // ISO date string
  nextreview: string;     // ISO date string
  days: number;
  previewtimegood: string;
  previewtimehard: string;
  noteLevel: NoteLevel;
}