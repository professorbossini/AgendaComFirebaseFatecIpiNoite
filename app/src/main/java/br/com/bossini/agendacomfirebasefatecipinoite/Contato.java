package br.com.bossini.agendacomfirebasefatecipinoite;

import com.google.firebase.database.Exclude;

/**
 * Created by rodrigo on 18/05/18.
 */

public class Contato {

    private String chave;
    private String nome, fone, email;

    @Exclude
    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Contato (String nome, String fone, String email){
        setNome(nome);
        setFone(fone);
        setEmail(email);
    }

    public Contato (String chave, String nome, String fone,
                                                String email){
        this (nome, fone, email);
        setChave(chave);
    }

    public Contato (){

    }

    @Override
    public String toString() {
        return "Contato{" +
                "chave='" + chave + '\'' +
                ", nome='" + nome + '\'' +
                ", fone='" + fone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
