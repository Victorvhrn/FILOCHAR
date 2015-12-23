#-*-coding: utf-8 -*-
import matplotlib.pyplot as plt

def carrega_tempos(arquivo):
	arq = open(arquivo,"r")
	tempos = []
	for linha in arq.readlines():
		tempos.append(float(linha))
	return tempos

def calcula_probabilidades(tempos):
	tempos.sort()
	tam = len(tempos)
	probs = []
	for i in xrange(tam):
		probs.append((float((i+1)-0.5)/100))
	return probs

def main():
	sair = False
	while not sair:
		arquivo = raw_input("Entre com o nome do arquivo:")
		if arquivo == "sair":
			break 
		nome = arquivo.split(".")[0]
		tempos = carrega_tempos(arquivo)
		probs =  calcula_probabilidades(tempos)
		plt.plot(tempos,probs,marker='x',label=nome)
	plt.legend(loc="best")
	plt.xlabel("Tempo(s)")
	plt.ylabel("Probabilidade")
	plt.show()
	

if __name__ == '__main__':
	main()