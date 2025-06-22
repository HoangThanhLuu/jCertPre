import {Injectable} from '@angular/core';
import {FileData} from '../model/FileData';
import {Observable} from 'rxjs';

@Injectable()
export class TransferFileService {
  fileData: any;
  mFile: FileData = new FileData();
  files: FileData[] = [];

  handleMultipleFiles(event: any) {
    const selectedFiles = event.target.files;
    if (!selectedFiles || selectedFiles.length === 0) {
      return;
    }
    this.files = []; // Reset the files array

    for (const file of selectedFiles) {
      const fileData = new FileData();
      fileData.file = file;
      fileData.name = file.name;
      fileData.type = file.type;
      fileData.size = Math.round(file.size / (1024 * 1024)); // Size in MB
      this.files.push(fileData);
    }
  }

  handleFileInput(event: any) {
    const file = event.target.files[0];
    this.handleFiles(file);
  }

  processFileObs(event: any): Observable<FileData> {
    const file = event.target.files[0];
    return this.processFile(file);
  }

  processFile(file: File): Observable<FileData> {
    return new Observable<FileData>((obs) => {
      if (!file) {
        obs.error('No file selected');
        return;
      }

      const reader = new FileReader();
      const fileData = new FileData();
      fileData.file = file;
      fileData.name = file.name;
      fileData.type = file.type;
      fileData.size = Math.round(file.size / 1024);

      reader.onload = (e) => {
        const result = e.target?.result;
        if (!result) {
          obs.error('Failed to read file');
          return;
        }

        obs.next(fileData);
        obs.complete();
      };

      reader.onerror = (error) => obs.error(error);
    });
  }


  handleFiles(file: any) {
    if (file) {
      const reader = new FileReader();
      reader.onload = (e) => {
        const result = e.target?.result;
        if (!result) return;

        this.fileData = file;
        this.mFile.name = file.name;
        this.mFile.type = file.type;
        this.mFile.size = Math.round(file.size / 1024);

        if (file.type.startsWith('image/')) {
          this.mFile.url = result as string; // Image in base64
        } else if (file.type === 'application/pdf') {
          // Convert PDF to base64
          const binary = new Uint8Array(result as ArrayBuffer);
          const base64String = btoa(String.fromCharCode(...binary));
          this.mFile.url = `data:application/pdf;base64,${base64String}`;
        }
      };

      if (file.type.startsWith('image/')) {
        reader.readAsDataURL(file);
      } else if (file.type === 'application/pdf') {
        reader.readAsArrayBuffer(file);
      } else {
        reader.readAsText(file);
      }
    }
  }

  allowDrop(event: any) {
    event.preventDefault();
  }

  handleDrop(event: any) {
    event.preventDefault();
    const files = event.dataTransfer.files[0];
    this.handleFiles(files);
  }

  reset() {
    this.fileData = undefined;
    this.mFile = new FileData();
  }

  removeFile(fileToRemove: FileData) {
    this.files = this.files.filter(file => file !== fileToRemove);
  }
}
